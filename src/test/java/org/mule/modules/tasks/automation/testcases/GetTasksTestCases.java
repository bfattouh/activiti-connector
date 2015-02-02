/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.tasks.automation.testcases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.modules.activiti.procesInstance.entities.ProcessInstance;
import org.mule.modules.activiti.task.entities.Task;
import org.mule.modules.activiti.task.entities.TasksWrapper;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * 
 * @author bfattouh
 * 
 */
public class GetTasksTestCases extends FunctionalMunitSuite {

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
		testData.put("deploymentFilePath",
				"src/test/resources/create-account.bar");
		testData.put("tenantId", "my-tenantId");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);
		deployment = (Deployment) resultEvent.getMessage().getPayload();
		assertNotNull(deployment);

		testData.clear();
		testData.put("processDefinitionKey", "create-account");
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
	public void testGetTasksByProcessInstanceId() throws Exception {
		testData.clear();
		testData.put("processInstanceId", processInstance.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-tasks", requestEvent);
		TasksWrapper tasksWrapper = (TasksWrapper) resultEvent.getMessage()
				.getPayload();
		assertNotNull(tasksWrapper);
		Task task = tasksWrapper.getTasks().get(0);
		assertTrue(task.getId() != null);
		assertTrue("Create account".equals(task.getName()));
		assertTrue(false == task.getSuspended());
		assertTrue("usertask1".equals(task.getTaskDefinitionKey()));
		assertTrue("my-tenantId".equals(task.getTenantId()));
	}

	@Test
	public void testGetTasksByProcessDefinition() throws Exception {
		testData.clear();
		testData.put("taskDefinitionKey", "usertask1");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-tasks", requestEvent);
		TasksWrapper tasksWrapper = (TasksWrapper) resultEvent.getMessage()
				.getPayload();
		assertNotNull(tasksWrapper);
		Task task = tasksWrapper.getTasks().get(0);
		assertTrue(task.getId() != null);
		assertTrue("Create account".equals(task.getName()));
		assertTrue(false == task.getSuspended());
		assertTrue("usertask1".equals(task.getTaskDefinitionKey()));
		assertTrue("my-tenantId".equals(task.getTenantId()));
	}

}
