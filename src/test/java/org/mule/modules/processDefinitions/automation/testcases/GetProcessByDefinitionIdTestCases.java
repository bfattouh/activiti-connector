/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.processDefinitions.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.modules.activiti.procesDefinition.entities.ProcessDefinition;
import org.mule.modules.activiti.procesDefinition.entities.ProcessDefinitionsWrapper;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * 
 * @author Bouchaib Fattouh - Appnovation Technologies
 * 
 */
public class GetProcessByDefinitionIdTestCases extends FunctionalMunitSuite {

	private Map<String, Object> testData = new HashMap<String, Object>();
	private Deployment deployment;
	private ProcessDefinition processDefinition;
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
		testData.put("tenantId", "myTenantId");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);
		deployment = (Deployment) resultEvent.getMessage().getPayload();
		assertNotNull(deployment);

		testData.clear();
		testData.put("name", "create-account");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-process-definition-by-name", requestEvent);
		ProcessDefinitionsWrapper processDefinitionWrapper = (ProcessDefinitionsWrapper) resultEvent
				.getMessage().getPayload();
		processDefinition = processDefinitionWrapper.getData().get(0);
	}

	@After
	public void tearDown() throws Exception {
		testData.clear();
		testData.put("deploymentId", deployment.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("delete-deployment-by-id", requestEvent);
		System.out.println("Deployment delete result : "
				+ (String) resultEvent.getMessage().getPayload());
	}

	@Test
	public void testGetProcessByDefinitionId() throws Exception {
		testData.clear();
		testData.put("processDefinitionId", processDefinition.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-process-by-definition-id", requestEvent);
		ProcessDefinition expectedProcessDefinition = (ProcessDefinition) resultEvent
				.getMessage().getPayload();
		assertNotNull(expectedProcessDefinition);
		assertEquals("create-account", expectedProcessDefinition.getName());
		assertEquals(deployment.getId(),
				expectedProcessDefinition.getDeploymentId());
		assertEquals(deployment.getTenantId(),
				expectedProcessDefinition.getTenantId());
		assertEquals(processDefinition.getId(),
				expectedProcessDefinition.getId());
	}

}
