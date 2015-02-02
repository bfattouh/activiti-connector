/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.deployments.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.modules.activiti.deployment.entities.DeploymentResource;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * 
 * @author Bouchaib Fattouh - Appnovation Technologies
 * 
 */
public class GetDeploymentResourceContentTestCases extends FunctionalMunitSuite {

	private Map<String, Object> testData = new HashMap<String, Object>();
	private Deployment deployment;
	private MuleEvent requestEvent;
	private MuleEvent resultEvent;
	private DeploymentResource deploymentResource;

	@Override
	protected String getConfigResources() {
		return "automation-test-flows.xml";
	}

	@Before
	public void setup() throws Exception {
		testData.put("deploymentFilePath",
				"src/test/resources/create-account.bar");
		testData.put("tenantId", "myTenantId");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);
		deployment = (Deployment) resultEvent.getMessage().getPayload();
		assertNotNull(deployment);

		testData.put("deploymentId", deployment.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-deployment-resources", requestEvent);
		String payload = (String) resultEvent.getMessage().getPayload();
		Class<List<DeploymentResource>> iClass = castClass(List.class);
		List<DeploymentResource> deploymentResources = DeploymentResource
				.unMarshalJSON(iClass, payload);
		deploymentResource = deploymentResources.get(0);
	}

	@After
	public void tearDown() throws Exception {
		testData.clear();
		testData.put("deploymentId", deployment.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("delete-deployment-by-id", requestEvent);
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<T> castClass(Class<?> aClass) {
		return (Class<T>) aClass;
	}

	@Test
	public void testGetDeploymentResourceContent() throws Exception {
		testData.clear();
		testData.put("deploymentId", deployment.getId());
		testData.put("resourceId", deploymentResource.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-deployment-resource-content", requestEvent);
		byte[] resourceContent = (byte[]) resultEvent.getMessage().getPayload();
		assertNotNull(resourceContent);
		String result = new String(resourceContent);
		String expectedResult = FileUtils.readFileToString(new File(
				"src/test/resources/accountcreation.bpmn20.xml"));
		assertEquals(expectedResult, result);
	}

}
