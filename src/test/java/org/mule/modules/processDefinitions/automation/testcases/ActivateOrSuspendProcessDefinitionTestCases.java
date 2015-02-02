/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.processDefinitions.automation.testcases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Date;
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
 * @author bfattouh
 * 
 */
public class ActivateOrSuspendProcessDefinitionTestCases extends
		FunctionalMunitSuite {

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
		testData.put("tenantId", "my-tenantId");
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
	public void testSuspendProcessDefinitionWithProcessDefinitionId()
			throws Exception {
		testData.clear();
		testData.put("name", "create-account");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-process-definition-by-name", requestEvent);
		ProcessDefinitionsWrapper processDefinitionsWrapper = (ProcessDefinitionsWrapper) resultEvent
				.getMessage().getPayload();
		ProcessDefinition processDefinition = processDefinitionsWrapper
				.getData().get(0);

		testData.clear();
		testData.put("processDefinitionId", processDefinition.getId());
		testData.put("action", "suspend");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("activate-or-suspend-process-definition",
				requestEvent);
		ProcessDefinition expectedProcessDefinition = (ProcessDefinition) resultEvent
				.getMessage().getPayload();
		assertNotNull(expectedProcessDefinition);
		assertTrue(expectedProcessDefinition.isSuspended());
	}

	@Test
	public void testSuspendProcessDefinitionWithEffectiveDate()
			throws Exception {
		testData.clear();
		testData.put("name", "create-account");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-process-definition-by-name", requestEvent);
		ProcessDefinitionsWrapper processDefinitionsWrapper = (ProcessDefinitionsWrapper) resultEvent
				.getMessage().getPayload();
		ProcessDefinition processDefinition = processDefinitionsWrapper
				.getData().get(0);

		testData.clear();
		testData.put("processDefinitionId", processDefinition.getId());
		testData.put("action", "suspend");
		testData.put("effectiveDate", new Date());
		testData.put("includeProcessInstances", true);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("activate-or-suspend-process-definition",
				requestEvent);
		ProcessDefinition expectedProcessDefinition = (ProcessDefinition) resultEvent
				.getMessage().getPayload();
		assertNotNull(expectedProcessDefinition);
		assertTrue(expectedProcessDefinition.isSuspended());
	}

	@Test
	public void testActivateProcessDefinitionWithEffectiveDate()
			throws Exception {
		testData.clear();
		testData.put("name", "create-account");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-process-definition-by-name", requestEvent);
		ProcessDefinitionsWrapper processDefinitionsWrapper = (ProcessDefinitionsWrapper) resultEvent
				.getMessage().getPayload();
		ProcessDefinition processDefinition = processDefinitionsWrapper
				.getData().get(0);

		// Suspend the process definition
		testData.clear();
		testData.put("processDefinitionId", processDefinition.getId());
		testData.put("action", "suspend");
		testData.put("effectiveDate", new Date());
		testData.put("includeProcessInstances", true);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("activate-or-suspend-process-definition",
				requestEvent);
		ProcessDefinition expectedProcessDefinition = (ProcessDefinition) resultEvent
				.getMessage().getPayload();
		assertNotNull(expectedProcessDefinition);
		assertTrue(expectedProcessDefinition.isSuspended());

		// Reactivate the same process definition
		testData.put("action", "activate");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("activate-or-suspend-process-definition",
				requestEvent);
		expectedProcessDefinition = (ProcessDefinition) resultEvent
				.getMessage().getPayload();
		assertNotNull(expectedProcessDefinition);
		assertFalse(expectedProcessDefinition.isSuspended());

	}

}
