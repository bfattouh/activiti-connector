/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.processInstances.automation.testcases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.ConnectorHelper;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.modules.activiti.procesInstance.entities.ProcessInstance;
import org.mule.modules.activiti.variable.entities.Variable;
import org.mule.modules.activiti.variable.entities.VariableValueType;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * 
 * @author bfattouh
 * 
 */
public class AddProcessInstanceVariableTestCases extends FunctionalMunitSuite {

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
	public void testAddProcessInstanceVariablesWithStringType()
			throws Exception {
		testData.clear();
		testData.put("processInstanceId", processInstance.getId());
		testData.put("name", "strProcVar");
		VariableValueType variableValueType = new VariableValueType("string",
				"test");
		testData.put("valueType", variableValueType);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("add-process-instance-variable-1", requestEvent);
		@SuppressWarnings("unchecked")
		List<Variable> result = (List<Variable>) resultEvent.getMessage()
				.getPayload();
		assertNotNull(result);
	}

	@Test
	public void testAddProcessInstanceVariablesWithIntegerType()
			throws Exception {
		testData.clear();
		testData.put("processInstanceId", processInstance.getId());
		testData.put("name", "intProcVar");
		VariableValueType variableValueType = new VariableValueType("integer",
				1234);
		testData.put("valueType", variableValueType);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("add-process-instance-variable-1", requestEvent);
		@SuppressWarnings("unchecked")
		List<Variable> result = (List<Variable>) resultEvent.getMessage()
				.getPayload();
		assertNotNull(result);
	}

	@Test
	public void testAddProcessInstanceVariablesWithBooleanType()
			throws Exception {
		testData.clear();
		testData.put("processInstanceId", processInstance.getId());
		testData.put("name", "boolProcVar");
		VariableValueType variableValueType = new VariableValueType("boolean",
				true);
		testData.put("valueType", variableValueType);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("add-process-instance-variable-1", requestEvent);
		@SuppressWarnings("unchecked")
		List<Variable> result = (List<Variable>) resultEvent.getMessage()
				.getPayload();
		assertNotNull(result);
	}

	@Test
	public void testAddProcessInstanceVariablesWithCollectionVariables()
			throws Exception {
		testData.clear();
		// First variable set
		testData.put("processInstanceId", processInstance.getId());
		testData.put("variableName1", "boolProcVar1");
		VariableValueType variableValueType1 = new VariableValueType("boolean",
				true);
		testData.put("valueType1", variableValueType1);

		// Second Variable set
		testData.put("variableName2", "intProcVar2");
		VariableValueType variableValueType2 = new VariableValueType("integer",
				1234);
		testData.put("valueType2", variableValueType2);

		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("add-process-instance-variable-2", requestEvent);
		@SuppressWarnings("unchecked")
		List<Variable> result = (List<Variable>) resultEvent.getMessage()
				.getPayload();
		assertNotNull(result);
		assertTrue(result.size() == 2);
	}

	@Test
	public void testAddProcessInstanceVariablesWithDateType() throws Exception {
		testData.clear();
		testData.put("processInstanceId", processInstance.getId());
		testData.put("name", "dateProcVar");
		VariableValueType variableValueType = new VariableValueType("date",
				ConnectorHelper.toISO8601Date(new Date()));
		testData.put("valueType", variableValueType);
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("add-process-instance-variable-1", requestEvent);
		@SuppressWarnings("unchecked")
		List<Variable> result = (List<Variable>) resultEvent.getMessage()
				.getPayload();
		assertNotNull(result);
	}

}
