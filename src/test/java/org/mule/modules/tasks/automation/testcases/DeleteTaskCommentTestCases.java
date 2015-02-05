/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.tasks.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.modules.activiti.procesInstance.entities.ProcessInstance;
import org.mule.modules.activiti.task.entities.Comment;
import org.mule.modules.activiti.task.entities.Task;
import org.mule.modules.activiti.task.entities.TasksWrapper;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * 
 * @author bfattouh
 * 
 */
public class DeleteTaskCommentTestCases extends FunctionalMunitSuite {

	private Map<String, Object> testData = new HashMap<String, Object>();
	private Deployment deployment;
	private ProcessInstance processInstance;
	private MuleEvent requestEvent;
	private MuleEvent resultEvent;
	private Task task;
	private Comment comment;

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

		testData.clear();
		testData.put("processInstanceId", processInstance.getId());
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-tasks", requestEvent);
		TasksWrapper tasksWrapper = (TasksWrapper) resultEvent.getMessage()
				.getPayload();
		assertNotNull(tasksWrapper);
		task = tasksWrapper.getTasks().get(0);	
		
		testData.clear();
		testData.put("taskId", task.getId());
		testData.put("message", "this is a task comment.");
		testData.put("saveProcessInstanceId", true);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-task-comment", requestEvent);
		comment = (Comment) resultEvent
				.getMessage().getPayload();
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
	public void testDeleteTaskComment() throws Exception {
		testData.clear();
		testData.put("taskId", task.getId());
		testData.put("commentId", comment.getId());
		testData.put("saveProcessInstanceId", true);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("delete-task-comment", requestEvent);
		String httpStatusCode = (String) resultEvent
				.getMessage().getPayload();
		assertNotNull(httpStatusCode);
		assertEquals("204", httpStatusCode);
	}
	
	@Test
	public void testDeleteTaskCommentWithInexistingTaskId() throws Exception {
		testData.clear();
		testData.put("taskId", "INEXISTING TASK ID");
		testData.put("commentId", comment.getId());
		testData.put("saveProcessInstanceId", true);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("delete-task-comment", requestEvent);
		String httpStatusCode = (String) resultEvent
				.getMessage().getPayload();
		assertNotNull(httpStatusCode);
		assertEquals("404", httpStatusCode);
	}
	
	@Test
	public void testDeleteTaskCommentWithInexistingCommentId() throws Exception {
		testData.clear();
		testData.put("taskId", task.getId());
		testData.put("commentId", "INEXISTING COMMENT ID");
		testData.put("saveProcessInstanceId", true);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("delete-task-comment", requestEvent);
		String httpStatusCode = (String) resultEvent
				.getMessage().getPayload();
		assertNotNull(httpStatusCode);
		assertEquals("404", httpStatusCode);
	}


}
