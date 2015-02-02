/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.processInstances.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.candidateStarter.model.CandidateStarter;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.modules.activiti.procesInstance.entities.ProcessInstance;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * 
 * @author bfattouh
 * 
 */
public class AddUserToProcessInstanceTestCases extends FunctionalMunitSuite {

	private Map<String, Object> testData = new HashMap<String, Object>();
	private Deployment deployment;
	private ProcessInstance processInstance;
	private MuleEvent requestEvent;
	private MuleEvent resultEvent;

	@Override
	protected String getConfigResources() {
		return "automation-test-flows.xml";
	}

	@Before
	public void setup() throws Exception {
		testData.put("deploymentFilePath", "src/test/resources/MyProcess3.bpmn");
		testData.put("tenantId", "my-tenantId");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);
		deployment = (Deployment) resultEvent.getMessage().getPayload();
		assertNotNull(deployment);

		testData.clear();
		testData.put("processDefinitionKey", "process-test");
		testData.put("tenantId", "my-tenantId");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("start-process-by-definition-key", requestEvent);
		processInstance = (ProcessInstance) resultEvent.getMessage()
				.getPayload();
		assertNotNull(processInstance);
	}

	@After
	public void tearDown() throws Exception {
		testData.clear();
		testData.put("processInstanceId", processInstance.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("delete-process-instance-by-id", requestEvent);

		testData.clear();
		testData.put("deploymentId", deployment.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		runFlow("delete-deployment-by-id", requestEvent);
	}

	@Test
	public void testAddUserToProcessInstance() throws Exception {
		testData.clear();
		testData.put("processInstanceId", processInstance.getId());
		testData.put("user", "bfattouh");
		testData.put("type", "participant");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("add-user-to-process-instance", requestEvent);
		CandidateStarter candidateStarter = (CandidateStarter) resultEvent
				.getMessage().getPayload();
		assertNotNull(candidateStarter);
		assertEquals("bfattouh", candidateStarter.getUser());
		assertEquals(null, candidateStarter.getGroup());
		assertEquals("participant", candidateStarter.getType());
	}

}
