/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.tasks.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.modules.activiti.procesInstance.entities.ProcessInstance;
import org.mule.modules.activiti.task.entities.Task;
import org.mule.modules.activiti.task.entities.TasksWrapper;
import org.mule.modules.activiti.variable.entities.Variable;
import org.mule.modules.activiti.variable.entities.VariableValueType;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * 
 * @author bfattouh
 * 
 */
public class GetTaskVariableByNameTestCases extends FunctionalMunitSuite {

	private Map<String, Object> testData = new HashMap<String, Object>();
	private Deployment deployment;
	private ProcessInstance processInstance;
	private MuleEvent requestEvent;
	private MuleEvent resultEvent;
	private Task task;
	private Variable variable;

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
		testData.put("variableName", "var1");
		VariableValueType valueType = new VariableValueType("string",
				"a value", "local");
		testData.put("valueType", valueType);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-task-variables", requestEvent);
		@SuppressWarnings("unchecked")
		List<Variable> variables = (List<Variable>) resultEvent.getMessage()
				.getPayload();
		variable = variables.get(0);
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
	public void testGetTasVariableByName() throws Exception {
		testData.clear();
		testData.put("taskId", task.getId());
		testData.put("scope", "local");
		testData.put("variableName", "var1");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-task-variable-by-name", requestEvent);
		Variable expectedVariable = (Variable) resultEvent.getMessage()
				.getPayload();
		assertNotNull(expectedVariable);
		assertEquals(variable.getName(), expectedVariable.getName());
		assertEquals(variable.getValue(), expectedVariable.getValue());
		assertEquals(variable.getType(), expectedVariable.getType());
		assertEquals(variable.getScope(), expectedVariable.getScope());
	}

}
