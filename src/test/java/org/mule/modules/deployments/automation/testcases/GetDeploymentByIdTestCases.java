/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.deployments.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * 
 * @author bfattouh
 * 
 */
public class GetDeploymentByIdTestCases extends FunctionalMunitSuite {

	private Map<String, Object> testData = new HashMap<String, Object>();
	private Deployment deployment;
	private MuleEvent requestEvent;
	private MuleEvent resultEvent;

	@Override
	protected String getConfigResources() {
		return "automation-test-flows.xml";
	}

	@Before
	public void setup() throws Exception {
		testData.put("deploymentFilePath",
				"src/test/resources/create-account.bar");
		testData.put("tenantId", "accreated2");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);
		deployment = (Deployment) resultEvent.getMessage().getPayload();
		assertNotNull(deployment);
	}

	@After
	public void tearDown() throws Exception {
		testData.clear();
		testData.put("deploymentId", deployment.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		runFlow("delete-deployment-by-id", requestEvent);
	}

	@Test
	public void testGetDeploymentById() throws Exception {
		testData.clear();
		testData.put("deploymentId", deployment.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		MuleEvent resultEvent = runFlow("get-deployment-by-id", requestEvent);
		Deployment expectedDeployment = (Deployment) resultEvent.getMessage()
				.getPayload();
		assertNotNull(expectedDeployment);
		assertEquals(expectedDeployment.getId(), deployment.getId());
		assertEquals(expectedDeployment.getName(), deployment.getName());
		assertEquals(expectedDeployment.getTenantId(), deployment.getTenantId());
		assertEquals(expectedDeployment.getUrl(), deployment.getUrl());
		assertNotNull(expectedDeployment.getDeploymentTime());
		assertEquals(expectedDeployment.getDeploymentTime(),
				deployment.getDeploymentTime());
	}

}
