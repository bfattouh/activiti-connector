/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.processDefinitions.automation.testcases;

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
import org.mule.modules.activiti.procesInstance.entities.ProcessInstance;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


/**
 * 
 * @author Bouchaib Fattouh - Appnovation Technologies
 *
 */
public class StartProcessByDefinitionIdTestCases extends FunctionalMunitSuite {

	private Map<String, Object> testData = new HashMap<String, Object>();
	private Deployment deployment; 
	private ProcessDefinition processDefinition;
	private ProcessInstance processInstance;
	private MuleEvent requestEvent;
	private MuleEvent resultEvent;

	@Override
    protected String getConfigResources()
    {
		return "automation-test-flows.xml";
	}
    
	@Before
    public void setup() throws Exception  
    {
		testData.put("deploymentFilePath", "src/test/resources/create-account.bar");
		testData.put("tenantId", "mytenantId");
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);	
		deployment = (Deployment)resultEvent.getMessage().getPayload();
		
		testData.clear();
    	testData.put("name", "create-account");	
    	requestEvent = testEvent(muleMessageWithPayload(testData));
    	resultEvent = runFlow("get-process-definition-by-name", requestEvent);	
		ProcessDefinitionsWrapper processDefinitionsWrapper = (ProcessDefinitionsWrapper)resultEvent.getMessage().getPayload();
		processDefinition = processDefinitionsWrapper.getData().get(0);
    }

    @After
    public void tearDown() throws Exception
    {
    	testData.clear();
    	testData.put("processInstanceId", processInstance.getId());	
    	requestEvent = testEvent(muleMessageWithPayload(testData));
    	resultEvent = runFlow("delete-process-by-id", requestEvent);   	
    	System.out.println("Process delete result : " + (String)resultEvent.getMessage().getPayload());
    	
    	testData.clear();
    	testData.put("deploymentId", deployment.getId());	
    	requestEvent = testEvent(muleMessageWithPayload(testData));
    	resultEvent = runFlow("delete-deployment-by-id", requestEvent);	
    	System.out.println("Deployment delete result : " + (String)resultEvent.getMessage().getPayload());	
    }


    @Test
    public void testStartPrecessByDefinitionId() throws Exception        
    {
    	testData.clear();
    	testData.put("processDefinitionId", processDefinition.getId());	
    	requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("start-process-by-definition-id", requestEvent);	
		processInstance = (ProcessInstance)resultEvent.getMessage().getPayload();
		assertNotNull(processInstance);
    }

}
