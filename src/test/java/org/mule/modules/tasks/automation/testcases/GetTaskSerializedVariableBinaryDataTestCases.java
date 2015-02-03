/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.tasks.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
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
import org.mule.modules.activiti.variable.entities.Variable;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * 
 * @author bfattouh
 * 
 */
public class GetTaskSerializedVariableBinaryDataTestCases extends FunctionalMunitSuite {

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
				"src/test/resources/create-account-2.bar");
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
		testData.put("variableName", "varAddress");
		//the file address.ser contains a serialized object of type Address
		//with values as:
		//street = "Rue de France"
		//city = "Casablanca"
		testData.put("variableFilePath", "src/test/resources/address.ser");
		testData.put("scope", "local");
		testData.put("type", "binary");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-task-binary-variable", requestEvent);
		variable = (Variable) resultEvent.getMessage()
				.getPayload();
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
	public void testGetTaskSerializedVariableBinaryData() throws Exception {
		testData.clear();
		testData.put("taskId", task.getId());
		testData.put("variableName", variable.getName());
		testData.put("scope", "local");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-task-variable-binary-data", requestEvent);
		byte[] variableBinaryData = (byte[]) resultEvent.getMessage()
				.getPayload();
		assertNotNull(variableBinaryData);
		// Retrieve the initial serialized value from variable.ser
		ByteArrayInputStream in = new ByteArrayInputStream(variableBinaryData);
		ObjectInputStream is = new ObjectInputStream(in);
		Address expectedAddress = (Address)is.readObject();
		assertNotNull(expectedAddress);
		assertEquals("Rue de France", expectedAddress.getStreet());
		assertEquals("Casablanca", expectedAddress.getCity());
	}

}
