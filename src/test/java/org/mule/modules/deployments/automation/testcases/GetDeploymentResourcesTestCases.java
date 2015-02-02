/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.deployments.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.modules.activiti.deployment.entities.DeploymentResource;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


/**
 * 
 * @author Bouchaib Fattouh - Appnovation Technologies
 *
 */
public class GetDeploymentResourcesTestCases extends FunctionalMunitSuite {


	private Map<String, Object> testData = new HashMap<String, Object>();
	private Deployment deployment;
	private MuleEvent requestEvent;
	private MuleEvent resultEvent;
	
	private static final String DEPLOYMENT_RESOURCES_JSON_PAYLOAD = "["+
			  "{"+
			   "\"id\":\"diagrams/my-process.bpmn20.xml\","+
			   "\"url\":\"http://localhost:8081/activiti-rest/service/repository/deployments/10/resources/diagrams%2Fmy-process.bpmn20.xml\","+
			   "\"dataUrl\":\"http://localhost:8081/activiti-rest/service/repository/deployments/10/resourcedata/diagrams%2Fmy-process.bpmn20.xml\","+
			   "\"mediaType\":\"text/xml\","+
			   "\"type\":\"processDefinition\""+
			  "},"+
			  "{"+
			    "\"id\":\"image.png\","+
			    "\"url\":\"http://localhost:8081/activiti-rest/service/repository/deployments/10/resources/image.png\","+
			    "\"dataUrl\":\"http://localhost:8081/activiti-rest/service/repository/deployments/10/resourcedata/image.png\","+
			    "\"mediaType\":\"image/png\","+
			    "\"type\":\"resource\""+
			  "}"+
			"]";


	@Override
    protected String getConfigResources()
    {
		return "automation-test-flows.xml";
	}
	
	@Before
    public void setup() throws Exception  
    {
		testData.put("deploymentFilePath", "src/test/resources/create-account.bar");
		testData.put("tenantId", "myTenantId");	
		requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("create-deployment", requestEvent);	
		deployment = (Deployment)resultEvent.getMessage().getPayload();
		assertNotNull(deployment);
    }
    

    @After
    public void tearDown() throws Exception
    {
    	testData.clear();
    	testData.put("deploymentId", deployment.getId());	
    	requestEvent = testEvent(muleMessageWithPayload(testData));
    	resultEvent = runFlow("delete-deployment-by-id", requestEvent);	
    }
    
	@SuppressWarnings("unchecked")
	private static <T> Class<T> castClass(Class<?> aClass) {
        return (Class<T>)aClass;
    }
	
	@Test
	public void testJson() throws Exception{
		Class<List<DeploymentResource>> clazz = castClass(List.class);
		List<DeploymentResource> deploymentResources = DeploymentResource.unMarshalJSON(clazz, DEPLOYMENT_RESOURCES_JSON_PAYLOAD);
		assertEquals(2, deploymentResources.size());
		DeploymentResource deploymentResource1 = deploymentResources.get(0);
		assertEquals("diagrams/my-process.bpmn20.xml", deploymentResource1.getId());
		assertEquals("http://localhost:8081/activiti-rest/service/repository/deployments/10/resources/diagrams%2Fmy-process.bpmn20.xml", 
															deploymentResource1.getUrl());
		assertEquals("text/xml", deploymentResource1.getMediaType());
		assertEquals("processDefinition", deploymentResource1.getType());
		assertEquals("http://localhost:8081/activiti-rest/service/repository/deployments/10/resourcedata/diagrams%2Fmy-process.bpmn20.xml", 
														deploymentResource1.getDataUrl());
		DeploymentResource deploymentResource2 = deploymentResources.get(1);
		assertEquals("image.png", deploymentResource2.getId());
		assertEquals("http://localhost:8081/activiti-rest/service/repository/deployments/10/resources/image.png", 
															deploymentResource2.getUrl());
		assertEquals("image/png", deploymentResource2.getMediaType());
		assertEquals("resource", deploymentResource2.getType());
		assertEquals("http://localhost:8081/activiti-rest/service/repository/deployments/10/resourcedata/image.png", 
														deploymentResource2.getDataUrl());
		String payloadResult = DeploymentResource.marshalJSON(clazz, deploymentResources);
		assertEquals(DEPLOYMENT_RESOURCES_JSON_PAYLOAD, payloadResult);
	}

    
    @Test
    public void testGetDeploymentResources() throws Exception        
    {
    	testData.clear();
    	testData.put("deploymentId", deployment.getId());	
    	requestEvent = testEvent(muleMessageWithPayload(testData));
		resultEvent = runFlow("get-deployment-resources", requestEvent);	
		String payload = (String)resultEvent.getMessage().getPayload();
		assertNotNull(payload);
		Class<List<DeploymentResource>> iClass = castClass(List.class);
		List<DeploymentResource> deploymentResources = DeploymentResource.unMarshalJSON(iClass, payload);
		assertFalse(deploymentResources.isEmpty());
		assertEquals(1, deploymentResources.size());
		DeploymentResource deploymentResource = deploymentResources.get(0);
		assertEquals("accountcreation.bpmn20.xml", deploymentResource.getId());
		assertEquals("http://localhost:8080/activiti-rest/service/repository/deployments/"+deployment.getId()+"/resources/accountcreation.bpmn20.xml", deploymentResource.getUrl());
		assertEquals("text/xml", deploymentResource.getMediaType());
		assertEquals("processDefinition", deploymentResource.getType());
		assertNull(deploymentResource.getDataUrl());
    }

}
