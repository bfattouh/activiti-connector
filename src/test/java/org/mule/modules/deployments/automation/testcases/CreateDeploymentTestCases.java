/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.deployments.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


/**
 * 
 * @author Bouchaib Fattouh - Appnovation Technologies
 *
 */
public class CreateDeploymentTestCases extends FunctionalMunitSuite {


	private Map<String, Object> testData = new HashMap<String, Object>();
	private Deployment deployment;
	private MuleEvent requestEvent;
	private MuleEvent resultEvent;


	@Override
    protected String getConfigResources()
    {
		return "automation-test-flows.xml";
	}
    

    @After
    public void tearDown() throws Exception
    {
    	testData.clear();
    	testData.put("deploymentId", deployment.getId());	
    	requestEvent = testEvent(muleMessageWithPayload(testData));
    	resultEvent = runFlow("delete-deployment-by-id", requestEvent);	
    	System.out.println("Deployment delete result : " + (String)resultEvent.getMessage().getPayload());
    }


    @Test
    public void testCreateDeploymentWithXML() throws Exception        
    {
    	testData.clear();
    	testData.put("deploymentFilePath", "src/test/resources/accountcreation.bpmn20.xml");
		testData.put("tenantId", "myTenantId");
    	requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);	
		deployment = (Deployment)resultEvent.getMessage().getPayload();
		assertNotNull(deployment);
		assertNotNull(deployment.getId());
		assertNotNull(deployment.getDeploymentTime());
		assertEquals("myTenantId", deployment.getTenantId());
		assertEquals("accountcreation.bpmn20.xml", deployment.getName());
    }
    
    @Test
    public void testCreateDeploymentWithBar() throws Exception        
    {
    	testData.clear();
    	testData.put("deploymentFilePath", "src/test/resources/create-account.bar");
		testData.put("tenantId", "myTenantId");
    	requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);	
		deployment = (Deployment)resultEvent.getMessage().getPayload();
		assertNotNull(deployment);
		assertNotNull(deployment.getId());
		assertNotNull(deployment.getDeploymentTime());
		assertEquals("myTenantId", deployment.getTenantId());
		assertEquals("create-account.bar", deployment.getName());
    }
    
    @Test
    public void testCreateDeploymentWithZip() throws Exception        
    {
    	testData.clear();
    	testData.put("deploymentFilePath", "src/test/resources/create-account.zip");
		testData.put("tenantId", "myTenantId");
    	requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);	
		deployment = (Deployment)resultEvent.getMessage().getPayload();
		assertNotNull(deployment);
		assertNotNull(deployment.getId());
		assertNotNull(deployment.getDeploymentTime());
		assertEquals("myTenantId", deployment.getTenantId());
		assertEquals("create-account.zip", deployment.getName());
    }

}
