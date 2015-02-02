/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.models.automation.testcases;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.model.entities.Model;
import org.mule.modules.activiti.model.entities.ModelsWrapper;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


/**
 * 
 * @author bfattouh
 *
 */
public class GetModelsTestCases extends FunctionalMunitSuite {

	private Map<String, Object> testData = new HashMap<String, Object>();
	private Model model;
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
	   	testData.put("name", "model-test");
    	requestEvent = testEvent(muleMessageWithPayload(testData));
    	resultEvent = runFlow("create-model", requestEvent);	
    	model = (Model)resultEvent.getMessage().getPayload();
    	assertNotNull(model);
    }

    @After
    public void tearDown() throws Exception
    {
    	testData.put("modelId", model.getId());
    	requestEvent = testEvent(muleMessageWithPayload(testData));
    	resultEvent = runFlow("delete-model-by-id", requestEvent);	
    }


    @Test
    public void testGetModels() throws Exception        
    {
    	requestEvent = testEvent(muleMessageWithPayload(null));
    	resultEvent = runFlow("get-models", requestEvent);	
    	ModelsWrapper modelsWrapper = (ModelsWrapper)resultEvent.getMessage().getPayload();
    	assertNotNull(modelsWrapper); 	
    	assertTrue(modelsWrapper.getModels().size() > 0);
    	Model expectedModel = modelsWrapper.getModelByName(model.getName());
    	assertNotNull(expectedModel);
    	assertEquals(model.getName(), expectedModel.getName());
    	assertEquals(model.getId(), expectedModel.getId());
    }
    
    @Test
    public void testGetModelById() throws Exception        
    {
    	testData.put("modelId", model.getId());
    	requestEvent = testEvent(muleMessageWithPayload(testData));
    	resultEvent = runFlow("get-model-by-id", requestEvent);	
    	Model expectedModel = (Model)resultEvent.getMessage().getPayload();
    	assertNotNull(model); 	
    	assertEquals(model.getName(), expectedModel.getName());
    	assertEquals(model.getId(), expectedModel.getId());
    }

}
