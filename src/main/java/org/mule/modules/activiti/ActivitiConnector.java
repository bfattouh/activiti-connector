/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti;

import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.MuleRuntimeException;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.display.Summary;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Processor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mule.api.annotations.ReconnectOn;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.rest.HttpMethod;
import org.mule.api.annotations.rest.RestCall;
import org.mule.api.annotations.rest.RestHeaderParam;
import org.mule.api.annotations.rest.RestQueryParam;
import org.mule.api.annotations.rest.RestUriParam;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpConnection;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.mule.api.annotations.rest.RestExceptionOn;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.mule.modules.activiti.candidateStarter.model.CandidateStarter;
import org.mule.modules.activiti.candidateStarter.model.CandidateStarterRequest;
import org.mule.modules.activiti.candidateStarter.model.CandidateStartersWrapper;
import org.mule.modules.activiti.deployment.entities.Deployment;
import org.mule.modules.activiti.deployment.entities.DeploymentResource;
import org.mule.modules.activiti.deployment.entities.DeploymentWrapper;
import org.mule.modules.activiti.model.entities.Model;
import org.mule.modules.activiti.model.entities.ModelRequest;
import org.mule.modules.activiti.model.entities.ModelsWrapper;
import org.mule.modules.activiti.procesDefinition.entities.ProcessDefinition;
import org.mule.modules.activiti.procesDefinition.entities.ProcessDefinitionCategoryRequest;
import org.mule.modules.activiti.procesDefinition.entities.ProcessDefinitionRequestAction;
import org.mule.modules.activiti.procesDefinition.entities.ProcessDefinitionsWrapper;
import org.mule.modules.activiti.procesInstance.entities.ProcessInstance;
import org.mule.modules.activiti.procesInstance.entities.ProcessInstanceQuery;
import org.mule.modules.activiti.procesInstance.entities.ProcessInstancesWrapper;
import org.mule.modules.activiti.procesInstance.entities.ProcessRequest;
import org.mule.modules.activiti.task.entities.Comment;
import org.mule.modules.activiti.task.entities.CommentRequest;
import org.mule.modules.activiti.task.entities.Task;
import org.mule.modules.activiti.task.entities.TaskActionRequest;
import org.mule.modules.activiti.task.entities.TaskRequest;
import org.mule.modules.activiti.task.entities.TasksWrapper;
import org.mule.modules.activiti.variable.entities.Variable;
import org.mule.modules.activiti.variable.entities.VariableRequest;
import org.mule.modules.activiti.variable.entities.VariableValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Anypoint Connector
 * 
 * @author Bouchaib Fattouh Appnovation Technologies
 */
@Connector(name = "activiti", schemaVersion = "1.0", friendlyName = "Activiti", minMuleVersion = "3.5.0", description = "Mule connector for Activiti server instance (V 5.17.0)")
@ReconnectOn(exceptions = { Exception.class })
public abstract class ActivitiConnector {

	private static final String DEFAULT_ACTIVITI_SERVER_URL = "http://localhost:8080";

	private static final String AUTHORIZATION_HEADER = "Authorization";

	private static final String ACCEPT_HEADER = "Accept";

	private static final String ALLOW_ENCODED_SLASH_PROPERTY = "org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH";

	private HttpConnection connection;

	private static final Logger logger = LoggerFactory
			.getLogger(ActivitiConnector.class);

	@RestHeaderParam("Authorization")
	private String authorization;

	/**
	 * The Activiti Server instance URL
	 */
	@RestUriParam("serverUrl")
	@Configurable
	@Placement(order = 1, group = "General Settings")
	@Default(value = DEFAULT_ACTIVITI_SERVER_URL)
	private String serverUrl;

	/**
	 * Custom processor that gets all deployments or in case of a query param
	 * gets the corresponding deployment
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-deployments}
	 * 
	 * @param aname
	 *            deployment name
	 * @param nameLike
	 *            deployment with the name like
	 * @param category
	 *            deployment category
	 * @param categoryNotEquals
	 *            deployment with the category not like
	 * @param tenantId
	 *            deployment with the tenantId
	 * @param tenantIdLike
	 *            deployment with tenant Id not like
	 * @param withoutTenantId
	 *            deployment without tenant Id
	 * @param sort
	 *            the sorting field
	 * @return The process instances wrapper
	 */
	@Processor(friendlyName = "Get Deployments")
	@Summary("This processor gets all deployments or in case of a query param gets the corresponding deployment")
	public DeploymentWrapper getDeployments(
			@Optional @RestQueryParam("name") String aname,
			@Optional @RestQueryParam("nameLike") String nameLike,
			@Optional @RestQueryParam("category") String category,
			@Optional @RestQueryParam("categoryNotEquals") String categoryNotEquals,
			@Optional @RestQueryParam("tenantId") String tenantId,
			@Optional @RestQueryParam("tenantIdLike") Boolean tenantIdLike,
			@Optional @RestQueryParam("withoutTenantId") Boolean withoutTenantId,
			@Optional @RestQueryParam("sort") String sort) {
		ResponseEntity<DeploymentWrapper> response = null;
		try {
			logger.info("Start getting Activiti deployments");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			HttpEntity<ProcessRequest> request = new HttpEntity<ProcessRequest>(
					headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/repository/deployments");
			URI uri = template.expand(serverUrl);
			URIBuilder builder = new URIBuilder(uri);
			List<NameValuePair> params = ConnectorHelper.getParams(aname,
					nameLike, category, categoryNotEquals, tenantId,
					tenantIdLike, withoutTenantId, sort);
			builder.addParameters(params);
			RestTemplate restTemplate = new RestTemplate();
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.GET;
			response = restTemplate.exchange(builder.build(), httpMthod,
					request, DeploymentWrapper.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			logger.info(
					"End getting Activiti deployments with response status code {}:",
					statuscode);

		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
		logger.debug("Get deployments response body: {}", response.getBody());
		return response.getBody();
	}

	;

	/**
	 * Custom processor that retrieves a deployed process by a deployment name
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-deployment-by-name}
	 * 
	 * @param aname
	 *            Deployment name
	 * @return DeploymentWrapper containing the Deployment found
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Deployment By Name")
	@Summary("This processor get Activiti deployment by deploymentName")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/deployments", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract DeploymentWrapper getDeploymentByName(
			@RestQueryParam("name") String aname) throws IOException;

	/**
	 * Custom processor that retrieves a deployed process by a deployment id
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-deployment-by-id}
	 * 
	 * @param deploymentId
	 *            Deployment id
	 * @return the Deployment found
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Deployment By Id")
	@Summary("This processor get Activiti deployment by deploymentId")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/deployments/{deploymentId}", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Deployment getDeploymentById(
			@RestUriParam("deploymentId") String deploymentId)
			throws IOException;

	/**
	 * Custom processor that deletes a deployed process by a deployment id
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:delete-deployment-by-id}
	 * 
	 * @param deploymentId
	 *            Deployment id
	 * @return String containing the deletion status code
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Delete Deployment By Id")
	@Summary("This processor deletes an Activiti deployment by deploymentId")
	public String deleteDeploymentById(
			@RestQueryParam("deploymentId") String deploymentId) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		String statusCode = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/repository/deployments/{deploymentId}");
		URI uri = template.expand(serverUrl, deploymentId);
		RestTemplate restTemplate = new RestTemplate();
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.DELETE);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			response = request.execute();
			statusCode = response.getStatusCode().toString();
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
		return statusCode;
	}

	/**
	 * Custom processor that creates a deployment using a packaged *.bar or
	 * *.zip (or *.xml bpm file) file 
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample activiti:create-deployment}
	 * 
	 * 
	 * @param deploymentFilePath
	 *            deployment file path
	 * @param tenantId
	 *            tenant id
	 * @return the Deployment object
	 */
	@Processor(friendlyName = "Create Deployment")
	@Summary("This processor creates a deployment using the deployment file")
	public Deployment createDeployment(
			@RestQueryParam("deploymentFilePath") String deploymentFilePath,
			@Optional @RestQueryParam("tenantId") String tenantId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add(AUTHORIZATION_HEADER, authorization);
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		File deploymentFile = new File(deploymentFilePath);
		Resource resource = new FileSystemResource(deploymentFile);
		formData.add("file", resource);
		formData.add("tenantId", tenantId);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(
				formData, headers);
		RestTemplate restTemplate = new RestTemplate();
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/repository/deployments");
		URI uri = template.expand(serverUrl);
		ResponseEntity<Deployment> response = restTemplate.postForEntity(uri,
				httpEntity, Deployment.class);
		Integer statuscode = response.getStatusCode().value();
		if (statuscode != 201) {
			throw new RuntimeException(
					"The request has been failed with code: " + statuscode);
		}
		return response.getBody();
	}

	/**
	 * Custom processor that retrieves process definitions
	 *  
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample activiti:get-process-definitions}
	 * 
	 * @param version
	 *            process definition version
	 * @param aname
	 *            process definition name
	 * @param nameLike
	 *            process definition name like
	 * @param key
	 *            process definition key
	 * @param keyLike
	 *            process definition key like
	 * @param resourceName
	 *            process definition resource name
	 * @param resourceNameLike
	 *            process definition resource name like
	 * @param categoryLike
	 *            process definition category like
	 * @param category
	 *            process definition category
	 * @param categoryNotEquals
	 *            process definition category not equals
	 * @param deploymentId
	 *            deployment Id
	 * @param startableByUser
	 *            process definition startable by user
	 * @param latest
	 *            process definition latest
	 * @param suspended
	 *            process definition suspended
	 * @param sort
	 *            the sorting field
	 * @return The process instances wrapper
	 */
	@Processor(friendlyName = "Get Process Definitions")
	@Summary("This processor get Activiti process definitions list")
	public ProcessDefinitionsWrapper getProcessDefinitions(
			@Optional @RestQueryParam("version") Integer version,
			@Optional @RestQueryParam("name") String aname,
			@Optional @RestQueryParam("nameLike") String nameLike,
			@Optional @RestQueryParam("key") String key,
			@Optional @RestQueryParam("keyLike") String keyLike,
			@Optional @RestQueryParam("resourceName") String resourceName,
			@Optional @RestQueryParam("resourceNameLike") String resourceNameLike,
			@Optional @RestQueryParam("category") String category,
			@Optional @RestQueryParam("categoryLike") String categoryLike,
			@Optional @RestQueryParam("categoryNotEquals") String categoryNotEquals,
			@Optional @RestQueryParam("deploymentId") String deploymentId,
			@Optional @RestQueryParam("startableByUser") String startableByUser,
			@Optional @RestQueryParam("latest") Boolean latest,
			@Optional @RestQueryParam("suspended") Boolean suspended,
			@Optional @RestQueryParam("sort") String sort) {
		ResponseEntity<ProcessDefinitionsWrapper> response = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			HttpEntity<ProcessRequest> request = new HttpEntity<ProcessRequest>(
					headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/repository/process-definitions");
			URI uri = template.expand(serverUrl);
			URIBuilder builder = new URIBuilder(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (version != null)
				params.add(ConnectorHelper.getNameValuePair("version",
						Integer.toString(version)));
			if (nameLike != null)
				params.add(ConnectorHelper.getNameValuePair("nameLike",
						nameLike));
			if (aname != null)
				params.add(ConnectorHelper.getNameValuePair("name", aname));
			if (key != null)
				params.add(ConnectorHelper.getNameValuePair("key", key));
			if (keyLike != null)
				params.add(ConnectorHelper.getNameValuePair("keyLike", keyLike));
			if (resourceName != null)
				params.add(ConnectorHelper.getNameValuePair("resourceName",
						resourceName));
			if (resourceNameLike != null)
				params.add(ConnectorHelper.getNameValuePair("resourceNameLike",
						resourceNameLike));
			if (category != null)
				params.add(ConnectorHelper.getNameValuePair("category",
						category));
			if (categoryLike != null)
				params.add(ConnectorHelper.getNameValuePair("categoryLike",
						categoryLike));
			if (categoryNotEquals != null)
				params.add(ConnectorHelper.getNameValuePair(
						"categoryNotEquals", categoryNotEquals));
			if (deploymentId != null)
				params.add(ConnectorHelper.getNameValuePair("deploymentId",
						deploymentId));
			if (startableByUser != null)
				params.add(ConnectorHelper.getNameValuePair("startableByUser",
						startableByUser));
			if (latest != null)
				params.add(ConnectorHelper.getNameValuePair("latest",
						Boolean.toString(latest)));
			if (suspended != null)
				params.add(ConnectorHelper.getNameValuePair("suspended",
						Boolean.toString(suspended)));
			if (sort != null)
				params.add(ConnectorHelper.getNameValuePair("sort", sort));
			builder.addParameters(params);
			RestTemplate restTemplate = new RestTemplate();
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.GET;
			response = restTemplate.exchange(builder.build(), httpMthod,
					request, ProcessDefinitionsWrapper.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
		return response.getBody();
	}

	/**
	 * Custom processor that retrieves process definition by name 
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample activiti:get-process-definition-by-name}
	 * 
	 * 
	 * @param aname
	 *            the process definition name
	 * @return the process definition wrapper containing the found process
	 *         definition
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Process Definition By Name")
	@Summary("This processor get Activiti process definition by name")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/process-definitions", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract ProcessDefinitionsWrapper getProcessDefinitionByName(
			@RestQueryParam("name") String aname) throws IOException;

	/**
	 * Custom processor that retrieves process by definition id 
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample activiti:get-process-by-definition-id}
	 * 
	 * @param processDefinitionId
	 *            process definition id
	 * @return the process defnition entity
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Process By Definition Id")
	@Summary("This processor get Activiti process by definition id")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/process-definitions/{processDefinitionId}", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract ProcessDefinition getProcessByDefinitionId(
			@RestUriParam("processDefinitionId") String processDefinitionId)
			throws IOException;

	/**
	 * Custom processor that retrieves a deployment resources list
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-deployment-resources}
	 * 
	 * @param deploymentId
	 *            Deployment id
	 * @return String response containing the resources collection
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Deployment Resources")
	@Summary("This processor get Activiti deployment resources list")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/deployments/{deploymentId}/resources", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract String getDeploymentResources(
			@RestUriParam("deploymentId") String deploymentId)
			throws IOException;

	/**
	 * Custom processor that retrieves a deployment resource content
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-deployment-resource-content}
	 * 
	 * @param deploymentId
	 *            Deployment id
	 * @param resourceId
	 *            resource Id
	 * @return byte[] response containing the resources content
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Deployment Resource Content")
	@Summary("This processor get Activiti deployment resource content")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/deployments/{deploymentId}/resourcedata/{resourceId}", method = HttpMethod.GET, exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract byte[] getDeploymentResourceContent(
			@RestUriParam("deploymentId") String deploymentId,
			@RestUriParam("resourceId") String resourceId) throws IOException;

	/**
	 * Custom processor that retrieves a process definition resource data
	 * resource content
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-process-definition-resource-content}
	 * 
	 * @param processDefinitionId
	 *            process definition id
	 * @return byte[] response containing the resources content
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Process Definition Resource Content")
	@Summary("This processor gets a process definition resource content")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/process-definitions/{processDefinitionId}/resourcedata", method = HttpMethod.GET, exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract byte[] getProcessDefinitionResourceContent(
			@RestUriParam("processDefinitionId") String processDefinitionId)
			throws IOException;

	/**
	 * Custom processor that retrieves a deployment resource
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-deployment-resource}
	 * 
	 * @param deploymentId
	 *            Deployment id
	 * @param resourceId
	 *            Resource id
	 * @return DeploymentResource response
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Deployment Resource")
	@Summary("This processor get Activiti deployment resources list")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/deployments/{deploymentId}/resources/{resourceId}", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract DeploymentResource getDeploymentResource(
			@RestUriParam("deploymentId") String deploymentId,
			@RestUriParam("resourceId") String resourceId) throws IOException;

	/**
	 * Custom processor that starts a process instance by the
	 * processDefinitionKey
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:start-process-by-definition-key}
	 * 
	 * @param processDefinitionKey
	 *            the process definition key
	 * @param businessKey
	 *            the business Key
	 * @param tenantId
	 *            the tenant Id
	 * @param variables
	 *            the variables collection
	 * @return the process instance started
	 * @throws MuleRuntimeException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Start Process By Definition Key")
	@Summary("This processor starts a process instance by the processDefinitionKey")
	public ProcessInstance startProcessByDefinitionKey(
			@RestQueryParam("processDefinitionKey") String processDefinitionKey,
			@Optional @RestQueryParam("businessKey") String businessKey,
			@Optional @RestQueryParam("tenantId") String tenantId,
			@Optional @RestQueryParam("variables") Map<String, String> variables) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			ProcessRequest processRequest = new ProcessRequest();
			processRequest = processRequest
					.withProcessDefinitionKey(processDefinitionKey);
			processRequest = processRequest.withBusinessKey(businessKey);
			processRequest = processRequest.withTenantId(tenantId);
			processRequest = processRequest.withVariables(variables);
			HttpEntity<ProcessRequest> request = new HttpEntity<ProcessRequest>(
					processRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/process-instances");
			URI uri = template.expand(serverUrl);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<ProcessInstance> response = restTemplate
					.postForEntity(uri, request, ProcessInstance.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 201) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that starts a process instance by the
	 * processDefinitionId
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:start-process-by-definition-id}
	 * 
	 * @param processDefinitionId
	 *            the process definition id
	 * @param businessKey
	 *            the business Key
	 * @param tenantId
	 *            the tenant Id
	 * @param variables
	 *            the variables collection
	 * @return the process instance started
	 * @throws MuleRuntimeException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Start Process By Definition Id")
	@Summary("This processor starts a process instance by the processDefinitionId")
	public ProcessInstance startProcessByDefinitionId(
			@RestQueryParam("processDefinitionId") String processDefinitionId,
			@Optional @RestQueryParam("businessKey") String businessKey,
			@Optional @RestQueryParam("variables") Map<String, String> variables) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			ProcessRequest processRequest = new ProcessRequest();
			processRequest = processRequest
					.withProcessDefinitionId(processDefinitionId);
			processRequest = processRequest.withBusinessKey(businessKey);
			processRequest = processRequest.withVariables(variables);
			HttpEntity<ProcessRequest> request = new HttpEntity<ProcessRequest>(
					processRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/process-instances");
			URI uri = template.expand(serverUrl);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<ProcessInstance> response = restTemplate
					.postForEntity(uri, request, ProcessInstance.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 201) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that deletes a process instance by the id
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:delete-process-by-id}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @return the HTTP response status code
	 * @throws MuleRuntimeException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Delete Process By Id")
	@Summary("This processor deletes a process instance by the processInstanceId")
	public String deleteProcessById(
			@RestQueryParam("processInstanceId") String processInstanceId) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		String statusCode = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}");
		URI uri = template.expand(serverUrl, processInstanceId);
		RestTemplate restTemplate = new RestTemplate();
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.DELETE);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			response = request.execute();
			statusCode = response.getStatusCode().toString();
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
		return statusCode;
	}

	/**
	 * Custom processor that gets a process instance by id
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-process-instance-by-id}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @return The process instance
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Process Instance By Id")
	@Summary("This processor gets a process instance by id")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract ProcessInstance getProcessInstanceById(
			@RestUriParam("processInstanceId") String processInstanceId)
			throws IOException;

	/**
	 * Custom processor that gets all process instances
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-process-instances}
	 * 
	 * @param id
	 *            the process instance id
	 * @param processDefinitionKey
	 *            the process definition key
	 * @param processDefinitionId
	 *            the process definition id
	 * @param buisnessKey
	 *            the business key
	 * @param involvedUser
	 *            the involved User
	 * @param suspended
	 *            the suspended value true or false
	 * @param superProcessInstanceId
	 *            the super process instance
	 * @param subProcessInstanceId
	 *            the sub process instance id
	 * @param excludeSubprocesses
	 *            the exclude subprocesses true or false
	 * @param includeProcessVariables
	 *            the include Process Variables true or false
	 * @param tenantId
	 *            the tenant Id
	 * @param tenantIdLike
	 *            the tenant Id like
	 * @param withoutTenantId
	 *            the without TenantId Id true or false
	 * @param sort
	 *            the sorting field
	 * @return The process instances wrapper
	 */
	@Processor(friendlyName = "Get Process Instances")
	@Summary("This processor gets all process instances")
	public ProcessInstancesWrapper getProcessInstances(
			@Optional @RestQueryParam("id") String id,
			@Optional @RestQueryParam("processDefinitionKey") String processDefinitionKey,
			@Optional @RestQueryParam("processDefinitionId") String processDefinitionId,
			@Optional @RestQueryParam("buisnessKey") String buisnessKey,
			@Optional @RestQueryParam("involvedUser") String involvedUser,
			@Optional @RestQueryParam("suspended") Boolean suspended,
			@Optional @RestQueryParam("superProcessInstanceId") String superProcessInstanceId,
			@Optional @RestQueryParam("subProcessInstanceId") String subProcessInstanceId,
			@Optional @RestQueryParam("excludeSubprocesses") Boolean excludeSubprocesses,
			@Optional @RestQueryParam("includeProcessVariables") String includeProcessVariables,
			@Optional @RestQueryParam("tenantId") String tenantId,
			@Optional @RestQueryParam("tenantIdLike") String tenantIdLike,
			@Optional @RestQueryParam("withoutTenantId") Boolean withoutTenantId,
			@Optional @RestQueryParam("sort") String sort) {
		ResponseEntity<ProcessInstancesWrapper> response = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			HttpEntity<ProcessRequest> request = new HttpEntity<ProcessRequest>(
					headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/process-instances");
			URI uri = template.expand(serverUrl);
			URIBuilder builder = new URIBuilder(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (id != null)
				params.add(ConnectorHelper.getNameValuePair("id", id));
			if (processDefinitionKey != null)
				params.add(ConnectorHelper.getNameValuePair(
						"processDefinitionKey", processDefinitionKey));
			if (processDefinitionId != null)
				params.add(ConnectorHelper.getNameValuePair(
						"processDefinitionId", processDefinitionId));
			if (buisnessKey != null)
				params.add(ConnectorHelper.getNameValuePair("buisnessKey",
						buisnessKey));
			if (involvedUser != null)
				params.add(ConnectorHelper.getNameValuePair("involvedUser",
						involvedUser));
			if (suspended != null)
				params.add(ConnectorHelper.getNameValuePair("suspended",
						Boolean.toString(suspended)));
			if (subProcessInstanceId != null)
				params.add(ConnectorHelper.getNameValuePair(
						"subProcessInstanceId", subProcessInstanceId));
			if (superProcessInstanceId != null)
				params.add(ConnectorHelper.getNameValuePair(
						"superProcessInstanceId", superProcessInstanceId));
			if (excludeSubprocesses != null)
				params.add(ConnectorHelper.getNameValuePair(
						"excludeSubprocesses",
						Boolean.toString(excludeSubprocesses)));
			if (includeProcessVariables != null)
				params.add(ConnectorHelper.getNameValuePair(
						"includeProcessVariables", includeProcessVariables));
			if (tenantId != null)
				params.add(ConnectorHelper.getNameValuePair("tenantId",
						tenantId));
			if (tenantIdLike != null)
				params.add(ConnectorHelper.getNameValuePair("tenantIdLike",
						tenantIdLike));
			if (withoutTenantId != null)
				params.add(ConnectorHelper.getNameValuePair("withoutTenantId",
						Boolean.toString(withoutTenantId)));
			if (sort != null)
				params.add(ConnectorHelper.getNameValuePair("sort", sort));
			builder.addParameters(params);
			RestTemplate restTemplate = new RestTemplate();
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.GET;
			response = restTemplate.exchange(builder.build(), httpMthod,
					request, ProcessInstancesWrapper.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
		return response.getBody();
	}

	/**
	 * Custom processor that deletes a process instance by id
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:delete-process-instance-by-id}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @return the http status code
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Delete Process Instance By Id")
	@Summary("This processor deletes a process instance by id")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}", method = HttpMethod.DELETE, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 204]") })
	public abstract String deleteProcessInstanceById(
			@RestUriParam("processInstanceId") String processInstanceId)
			throws IOException;

	/**
	 * Custom processor that activates or suspends a process definition
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:activate-or-suspend-process-definition}
	 * 
	 * @param processDefinitionId
	 *            The id of the process definition to activate/suspend.
	 * @param action
	 *            the action to process: suspend or activate
	 * @param includeProcessInstances
	 *            boolean for including process instances
	 * @param effectiveDate
	 *            the date when the activate/suspend must be effective
	 * @return the process definition
	 * @throws MuleRuntimeException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Activate Or Suspend Process Definition")
	@Summary("This processor suspends a process definition by the processDefinitionId")
	public ProcessDefinition activateOrSuspendProcessDefinition(
			@RestQueryParam("processDefinitionId") String processDefinitionId,
			@RestQueryParam("action") String action,
			@Optional @RestQueryParam("includeProcessInstances") Boolean includeProcessInstances,
			@Optional @RestQueryParam("effectiveDate") Date effectiveDate) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION_HEADER, authorization);
		headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
		ProcessDefinitionRequestAction pdra = new ProcessDefinitionRequestAction();
		pdra = pdra.withAction(action);
		if (effectiveDate != null)
			pdra = pdra.withDate(ConnectorHelper.toISO8601Date(effectiveDate));
		if (includeProcessInstances != null)
			pdra = pdra.withIncludeProcessInstances(includeProcessInstances);
		HttpEntity<ProcessDefinitionRequestAction> request = new HttpEntity<ProcessDefinitionRequestAction>(
				pdra, headers);
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/repository/process-definitions/{processDefinitionId}");
		URI uri = template.expand(serverUrl, processDefinitionId);
		RestTemplate restTemplate = new RestTemplate();
		org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.PUT;
		ResponseEntity<ProcessDefinition> response = restTemplate.exchange(uri,
				method, request, ProcessDefinition.class);
		Integer statuscode = response.getStatusCode().value();
		if (statuscode != 200) {
			throw new RuntimeException(
					"The request has been failed with code: " + statuscode);
		}
		return response.getBody();
	}

	/**
	 * Custom processor that updates a process definition category
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:update-process-definition-category}
	 * 
	 * @param processDefinitionId
	 *            The id of the process definition to activate/suspend.
	 * @param category
	 *            the category new value
	 * @return the process definition
	 * @throws MuleRuntimeException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Update Process Definition Category")
	@Summary("This processor updates a process definition category")
	public ProcessDefinition updateProcessDefinitionCategory(
			@RestQueryParam("processDefinitionId") String processDefinitionId,
			@RestQueryParam("category") String category) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION_HEADER, authorization);
		headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
		ProcessDefinitionCategoryRequest pdcr = new ProcessDefinitionCategoryRequest();
		pdcr = pdcr.withCategory(category);
		HttpEntity<ProcessDefinitionCategoryRequest> request = new HttpEntity<ProcessDefinitionCategoryRequest>(
				pdcr, headers);
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/repository/process-definitions/{processDefinitionId}");
		URI uri = template.expand(serverUrl, processDefinitionId);
		RestTemplate restTemplate = new RestTemplate();
		org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.PUT;
		ResponseEntity<ProcessDefinition> response = restTemplate.exchange(uri,
				method, request, ProcessDefinition.class);
		Integer statuscode = response.getStatusCode().value();
		if (statuscode != 200) {
			throw new RuntimeException(
					"The request has been failed with code: " + statuscode);
		}
		return response.getBody();
	}

	/**
	 * Custom processor that gets process definition candidate starters
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-process-definition-candidate-starters}
	 * 
	 * @param processDefinitionId
	 *            the process definition id
	 * @return CandidateStartersWrapper response
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Process Definition Candidate Starters")
	@Summary("This processor gets a process definition candidate starters")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/process-definitions/{processDefinitionId}/identitylinks", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract CandidateStartersWrapper getProcessDefinitionCandidateStarters(
			@RestUriParam("processDefinitionId") String processDefinitionId)
			throws IOException;

	/**
	 * Custom processor that adds a candidate starter to a process definition
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:add-candidate-starter-to-process-definition}
	 * 
	 * @param processDefinitionId
	 *            the process definition id
	 * @param user
	 *            the user candidate starter
	 * @param groupId
	 *            the groupid candidate starter
	 * @return the candidate starter response
	 * @throws MuleRuntimeException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Add Candidate Starter To Process Definition")
	@Summary("This processor adds a candidate starter to a process definition")
	public CandidateStarter addCandidateStarterToProcessDefinition(
			@RestQueryParam("processDefinitionId") String processDefinitionId,
			@Optional @RestQueryParam("user") String user,
			@Optional @RestQueryParam("groupId") String groupId) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			CandidateStarterRequest candidateStarterRequest = new CandidateStarterRequest();
			candidateStarterRequest = candidateStarterRequest.withUser(user);
			candidateStarterRequest = candidateStarterRequest
					.withGroupId(groupId);
			HttpEntity<CandidateStarterRequest> request = new HttpEntity<CandidateStarterRequest>(
					candidateStarterRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/repository/process-definitions/{processDefinitionId}/identitylinks");
			URI uri = template.expand(serverUrl, processDefinitionId);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<CandidateStarter> response = restTemplate
					.postForEntity(uri, request, CandidateStarter.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 201) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that deletes candidate starter from process definition
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:delete-process-definition-candidate-starter}
	 * 
	 * @param processDefinitionId
	 *            the process definition id
	 * @param family
	 *            either users or groups
	 * @param identityId
	 *            either userId or groupId
	 * @return The response HTTP code
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Delete Process Definition Candidate Starter")
	@Summary("This processor deletes a process definition candidate starter")
	public String deleteProcessDefinitionCandidateStarter(
			@RestUriParam("processDefinitionId") String processDefinitionId,
			@RestUriParam("family") String family,
			@RestUriParam("identityId") String identityId) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		String statusCode = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/repository/process-definitions/{processDefinitionId}/identitylinks/{family}/{identityId}");
		URI uri = template.expand(serverUrl, processDefinitionId, family,
				identityId);
		RestTemplate restTemplate = new RestTemplate();
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.DELETE);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			response = request.execute();
			statusCode = response.getStatusCode().toString();
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
		return statusCode;
	}

	/**
	 * Custom processor that gets processDefinition candidate starter
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-process-definition-candidate-starter}
	 * 
	 * @param processDefinitionId
	 *            the process definition id
	 * @param family
	 *            either users or groups
	 * @param identityId
	 *            either userId or groupId
	 * @return CandidateStarter response
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Process Definition Candidate Starter")
	@Summary("This processor gets a process definition candidate starter")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/process-definitions/{processDefinitionId}/identitylinks/{family}/{identityId}", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract CandidateStarter getProcessDefinitionCandidateStarter(
			@RestUriParam("processDefinitionId") String processDefinitionId,
			@RestUriParam("family") String family,
			@RestUriParam("identityId") String identityId) throws IOException;

	/**
	 * Custom processor that gets models
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-models}
	 * 
	 * @param id
	 *            the pmodel id
	 * @param category
	 *            the category
	 * @param categoryLike
	 *            the model category Like
	 * @param categoryNotEquals
	 *            the model category Not Equals
	 * @param aname
	 *            the model name
	 * @param nameLike
	 *            the model name like
	 * @param key
	 *            the model key
	 * @param deploymentId
	 *            the deployment id
	 * @param version
	 *            the model version
	 * @param latestVersion
	 *            the katest version
	 * @param deployed
	 *            the deployed
	 * @param tenantId
	 *            the tenant id
	 * @param tenantIdLike
	 *            the tenant id like
	 * @param withoutTenantId
	 *            the tenant id id
	 * @param sort
	 *            the sorting field
	 * @return TasksWrapper response
	 */
	@Processor(friendlyName = "Ge Models")
	@Summary("Custom processor that gets models")
	public ModelsWrapper getModels(
			@Optional @RestQueryParam("id") String id,
			@Optional @RestQueryParam("category") String category,
			@Optional @RestQueryParam("categoryLike") String categoryLike,
			@Optional @RestQueryParam("categoryNotEquals") String categoryNotEquals,
			@Optional @RestQueryParam("name") String aname,
			@Optional @RestQueryParam("nameLike") String nameLike,
			@Optional @RestQueryParam("key") String key,
			@Optional @RestQueryParam("deploymentId") String deploymentId,
			@Optional @RestQueryParam("version") Integer version,
			@Optional @RestQueryParam("latestVersion") Boolean latestVersion,
			@Optional @RestQueryParam("deployed") Boolean deployed,
			@Optional @RestQueryParam("tenantId") String tenantId,
			@Optional @RestQueryParam("tenantIdLike") String tenantIdLike,
			@Optional @RestQueryParam("withoutTenantId") Boolean withoutTenantId,
			@Optional @RestQueryParam("sort") String sort) {
		ResponseEntity<ModelsWrapper> response = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			HttpEntity<ProcessRequest> request = new HttpEntity<ProcessRequest>(
					headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/repository/models");
			URI uri = template.expand(serverUrl);
			URIBuilder builder = new URIBuilder(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (id != null)
				params.add(ConnectorHelper.getNameValuePair("id", id));
			if (category != null)
				params.add(ConnectorHelper.getNameValuePair("category",
						category));
			if (categoryLike != null)
				params.add(ConnectorHelper.getNameValuePair("categoryLike",
						categoryLike));
			if (categoryNotEquals != null)
				params.add(ConnectorHelper.getNameValuePair(
						"categoryNotEquals", categoryNotEquals));
			if (aname != null)
				params.add(ConnectorHelper.getNameValuePair("name", aname));
			if (nameLike != null)
				params.add(ConnectorHelper.getNameValuePair("nameLike",
						nameLike));
			if (key != null)
				params.add(ConnectorHelper.getNameValuePair("key", key));
			if (deploymentId != null)
				params.add(ConnectorHelper.getNameValuePair("deploymentId",
						deploymentId));
			if (version != null)
				params.add(ConnectorHelper.getNameValuePair("version",
						Integer.toString(version)));
			if (latestVersion != null)
				params.add(ConnectorHelper.getNameValuePair("latestVersion",
						Boolean.toString(latestVersion)));
			if (deployed != null)
				params.add(ConnectorHelper.getNameValuePair("deployed",
						Boolean.toString(deployed)));
			if (tenantId != null)
				params.add(ConnectorHelper.getNameValuePair("tenantId",
						tenantId));
			if (tenantIdLike != null)
				params.add(ConnectorHelper.getNameValuePair("tenantIdLike",
						tenantIdLike));
			if (withoutTenantId != null)
				params.add(ConnectorHelper.getNameValuePair("withoutTenantId",
						Boolean.toString(withoutTenantId)));
			if (sort != null)
				params.add(ConnectorHelper.getNameValuePair("sort", sort));
			builder.addParameters(params);
			RestTemplate restTemplate = new RestTemplate();
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.GET;
			response = restTemplate.exchange(builder.build(), httpMthod,
					request, ModelsWrapper.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
		return response.getBody();
	}

	/**
	 * Custom processor that creates a model
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:create-model}
	 * 
	 * @param aname
	 *            the model name
	 * @param key
	 *            model key
	 * @param category
	 *            the model category
	 * @param version
	 *            the model version
	 * @param metaInfo
	 *            the model meta info
	 * @param deploymentId
	 *            the deployment id
	 * @param tenantId
	 *            the tenant id
	 * @return The Model response
	 */
	@Processor(friendlyName = "Create Model")
	@Summary("Custom processor that creates a model")
	public Model createModel(@RestQueryParam("name") String aname,
			@Optional @RestQueryParam("key") String key,
			@Optional @RestQueryParam("category") String category,
			@Optional @RestQueryParam("version") Integer version,
			@Optional @RestQueryParam("metaInfo") String metaInfo,
			@Optional @RestQueryParam("deploymentId") String deploymentId,
			@Optional @RestQueryParam("tenantId") String tenantId) {
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			ModelRequest modelRequest = new ModelRequest().withName(aname);
			if (key != null)
				modelRequest = modelRequest.withKey(key);
			if (category != null)
				modelRequest = modelRequest.withCategory(category);
			if (version != null)
				modelRequest = modelRequest.withVersion(version);
			if (metaInfo != null)
				modelRequest = modelRequest.withMetaInfo(metaInfo);
			if (deploymentId != null)
				modelRequest = modelRequest.withDeploymentId(deploymentId);
			if (tenantId != null)
				modelRequest = modelRequest.withTenantId(tenantId);
			HttpEntity<ModelRequest> request = new HttpEntity<ModelRequest>(
					modelRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/repository/models");
			URI uri = template.expand(serverUrl);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Model> response = restTemplate.postForEntity(uri,
					request, Model.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 201) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that deletes a model by id
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:delete-model-by-id}
	 * 
	 * @param modelId
	 *            the model id
	 * @return the http status code
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Delete Model By Id")
	@Summary("This processor deletes a model by id")
	public String deleteModelById(@RestQueryParam("modelId") String modelId) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		String statusCode = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/repository/models/{modelId}");
		URI uri = template.expand(serverUrl, modelId);
		RestTemplate restTemplate = new RestTemplate();
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.DELETE);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			response = request.execute();
			statusCode = response.getStatusCode().toString();
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
		return statusCode;
	}

	/**
	 * Custom processor that gets a model by id
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-model-by-id}
	 * 
	 * @param modelId
	 *            the model id
	 * @return Model response
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Model By Id")
	@Summary("This processor gets a model by id")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/repository/models/{modelId}", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Model getModelById(@RestUriParam("modelId") String modelId)
			throws IOException;

	/**
	 * Custom processor that updates a model
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:update-model}
	 * 
	 * @param aname
	 *            the model name
	 * @param modelId
	 *            the model id
	 * @param key
	 *            model key
	 * @param category
	 *            the model category
	 * @param version
	 *            the model version
	 * @param metaInfo
	 *            the model meta info
	 * @param deploymentId
	 *            the deployment id
	 * @param tenantId
	 *            the tenant id
	 * @return The Model response
	 * @throws MuleRuntimeException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Update Model")
	@Summary("This processor updates a model")
	public Model updateModel(@RestQueryParam("name") String aname,
			@RestQueryParam("modelId") String modelId,
			@Optional @RestQueryParam("key") String key,
			@Optional @RestQueryParam("category") String category,
			@Optional @RestQueryParam("version") Integer version,
			@Optional @RestQueryParam("metaInfo") String metaInfo,
			@Optional @RestQueryParam("deploymentId") String deploymentId,
			@Optional @RestQueryParam("tenantId") String tenantId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION_HEADER, authorization);
		headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
		ModelRequest modelRequest = new ModelRequest().withName(aname);
		if (category != null)
			modelRequest = modelRequest.withCategory(category);
		if (version != null)
			modelRequest = modelRequest.withVersion(version);
		if (metaInfo != null)
			modelRequest = modelRequest.withMetaInfo(metaInfo);
		if (deploymentId != null)
			modelRequest = modelRequest.withDeploymentId(deploymentId);
		if (tenantId != null)
			modelRequest = modelRequest.withTenantId(tenantId);
		if (key != null)
			modelRequest = modelRequest.withKey(key);
		HttpEntity<ModelRequest> request = new HttpEntity<ModelRequest>(
				modelRequest, headers);
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/repository/models/{modelId}");
		URI uri = template.expand(serverUrl, modelId);
		RestTemplate restTemplate = new RestTemplate();
		org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.PUT;
		ResponseEntity<Model> response = restTemplate.exchange(uri, method,
				request, Model.class);
		Integer statuscode = response.getStatusCode().value();
		if (statuscode != 200) {
			throw new RuntimeException(
					"The request has been failed with code: " + statuscode);
		}
		return response.getBody();
	}

	/**
	 * Custom processor that sets a model editor source
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:set-model-editor-source}
	 * 
	 * @param modelId
	 *            the model id
	 * @param editorSourceFilePath
	 *            the editor source file path
	 * @return The model’s raw editor source
	 * @throws MuleRuntimeException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Set Model Editor Source")
	@Summary("This processor sets a model editor source")
	public String setModelEditorSource(
			@RestQueryParam("modelId") String modelId,
			@RestQueryParam("editorSourceFilePath") String editorSourceFilePath) {
		Integer statuscode = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add(AUTHORIZATION_HEADER, authorization);
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			File deploymentFile = new File(editorSourceFilePath);
			Resource resource = new FileSystemResource(deploymentFile);
			formData.add("file-part", resource);
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(
					formData, headers);
			CustomRestTemplate restTemplate = new CustomRestTemplate();
			restTemplate
					.setDefaultResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/repository/models/{modelId}/source");
			URI uri = template.expand(serverUrl, modelId);
			org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.PUT;
			ResponseEntity<byte[]> response = restTemplate.exchange(uri,
					method, httpEntity, byte[].class);
			statuscode = response.getStatusCode().value();
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
		return String.valueOf(statuscode);
	}

	/**
	 * Custom processor that sets a model editor source
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:set-model-extra-editor-source}
	 * 
	 * @param modelId
	 *            the model id
	 * @param extraEditorSourceFilePath
	 *            the extra editor source file path
	 * @return The model’s raw editor source
	 */
	@Processor(friendlyName = "Set Model Extra Editor Source")
	@Summary("This processor sets a model extra editor source")
	public String setModelExtraEditorSource(
			@RestQueryParam("modelId") String modelId,
			@RestQueryParam("extraEditorSourceFilePath") String extraEditorSourceFilePath) {
		Integer statuscode = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add(AUTHORIZATION_HEADER, authorization);
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			File deploymentFile = new File(extraEditorSourceFilePath);
			Resource resource = new FileSystemResource(deploymentFile);
			formData.add("file-part", resource);
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(
					formData, headers);
			CustomRestTemplate restTemplate = new CustomRestTemplate();
			restTemplate
					.setDefaultResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/repository/models/{modelId}/source-extra");
			URI uri = template.expand(serverUrl, modelId);
			org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.PUT;
			ResponseEntity<byte[]> response = restTemplate.exchange(uri,
					method, httpEntity, byte[].class);
			statuscode = response.getStatusCode().value();
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
		return String.valueOf(statuscode);
	}

	/**
	 * Custom processor that retrieves a model editor resource
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-model-editor-source}
	 * 
	 * @param modelId
	 *            the model id
	 * @return byte[] response containing the model editor source or 404 status
	 *         code if the resource was not found
	 */
	@Processor(friendlyName = "Get Model Editor Source")
	@Summary("This processor that retrieves a model editor resource")
	public String getModelEditorSource(@RestQueryParam("modelId") String modelId) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/repository/models/{modelId}/source");
		URI uri = template.expand(serverUrl, modelId);
		CustomRestTemplate restTemplate = new CustomRestTemplate();
		restTemplate
				.setDefaultResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.GET);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			response = request.execute();
			if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return String.valueOf(response.getStatusCode().value());
			}
			return IOUtils.toString(response.getBody());
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that retrieves a model extra editor resource
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-model-extra-editor-source}
	 * 
	 * @param modelId
	 *            the model id
	 * @return String response containing the extra editor source
	 */
	@Processor(friendlyName = "Get Model Extra Editor Source")
	@Summary("This processor that retrieves a model extra editor resource")
	public String getModelExtraEditorSource(
			@RestQueryParam("modelId") String modelId) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/repository/models/{modelId}/source-extra");
		URI uri = template.expand(serverUrl, modelId);
		CustomRestTemplate restTemplate = new CustomRestTemplate();
		restTemplate
				.setDefaultResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.GET);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			response = request.execute();
			if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return String.valueOf(response.getStatusCode().value());
			}
			return IOUtils.toString(response.getBody());
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that queries process instances
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:query-process-instances}
	 * 
	 * @param processDefinitionKey
	 *            the process definition key
	 * @param variables
	 *            the collection if variables
	 * @return the process instances wrapper
	 */
	@Processor(friendlyName = "Query Process Instances")
	@Summary("This processor starts a process instance by the processDefinitionKey")
	public ProcessInstancesWrapper queryProcessInstances(
			@RestQueryParam("processDefinitionKey") String processDefinitionKey,
			@Optional @RestQueryParam("variables") Map<String, String> variables) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			ProcessInstanceQuery processInstanceQuery = new ProcessInstanceQuery();
			processInstanceQuery = processInstanceQuery
					.withProcessDefinitionKey(processDefinitionKey);
			processInstanceQuery = processInstanceQuery
					.withVariables(variables);
			HttpEntity<ProcessInstanceQuery> request = new HttpEntity<ProcessInstanceQuery>(
					processInstanceQuery, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/query/process-instances");
			URI uri = template.expand(serverUrl);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<ProcessInstancesWrapper> response = restTemplate
					.postForEntity(uri, request, ProcessInstancesWrapper.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor returns the process instance diagram
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-process-instance-diagram}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @return The process instance diagram stream
	 */
	@Processor(friendlyName = "Get Process Instance Diagram")
	@Summary("This processor gets a process instance diagram")
	public InputStream getProcessInstanceDiagram(
			@RestUriParam("processInstanceId") String processInstanceId) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}/diagram");
		URI uri = template.expand(serverUrl, processInstanceId);
		CustomRestTemplate restTemplate = new CustomRestTemplate();
		restTemplate
				.setDefaultResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.GET);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			response = request.execute();
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that gets process instance candidate starters
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-process-instance-candidate-starters}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @return String containing the Candidate Starters
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Process Instance Candidate Starters")
	@Summary("This processor gets a process instance candidate starters")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}/identitylinks", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract String getProcessInstanceCandidateStarters(
			@RestUriParam("processInstanceId") String processInstanceId)
			throws IOException;

	/**
	 * Custom processor that adds a user to a process instance
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:add-user-to-process-instance}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @param user
	 *            the user involved in the process instance
	 * @param type
	 *            the user type
	 * @return the candidate starter response
	 * @throws MuleRuntimeException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Add User To Process Instance")
	@Summary("This processor adds a user to a process instance")
	public CandidateStarter addUserToProcessInstance(
			@RestQueryParam("processInstanceId") String processInstanceId,
			@RestQueryParam("user") String user,
			@RestQueryParam("type") String type) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			CandidateStarterRequest candidateStarterRequest = new CandidateStarterRequest();
			candidateStarterRequest = candidateStarterRequest.withUser(user);
			candidateStarterRequest = candidateStarterRequest.withType(type);
			HttpEntity<CandidateStarterRequest> request = new HttpEntity<CandidateStarterRequest>(
					candidateStarterRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}/identitylinks");
			URI uri = template.expand(serverUrl, processInstanceId);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<CandidateStarter> response = restTemplate
					.postForEntity(uri, request, CandidateStarter.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 201) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that removes a user from a process instance
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:remove-user-from-process-instance}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @param user
	 *            the user to remove from process instance
	 * @param type
	 *            the user type to remove from process instance
	 * @return The removed user
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Remove User From Process Instance")
	@Summary("This processor removes a user from a process instance")
	public String removeUserFromProcessInstance(
			@RestUriParam("processInstanceId") String processInstanceId,
			@RestQueryParam("user") String user,
			@RestQueryParam("type") String type) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		String statusCode = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}/identitylinks/users/{user}/{type}");
		URI uri = template.expand(serverUrl, processInstanceId, user, type);
		RestTemplate restTemplate = new RestTemplate();
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.DELETE);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			response = request.execute();
			statusCode = response.getStatusCode().toString();
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
		return statusCode;
	}

	/**
	 * Custom processor that gets process instance variables
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-process-instance-variables}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @return A collection of variables containing the variables
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Processor(friendlyName = "Get Process Instance Variables")
	@Summary("This processor gets a process instance variables")
	public List<Variable> getProcessInstanceVariables(
			@RestUriParam("processInstanceId") String processInstanceId) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			RestTemplate restTemplate = new RestTemplate();
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}/variables");
			URI uri = template.expand(serverUrl, processInstanceId);
			org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.GET;
			HttpEntity request = new HttpEntity(headers);
			ResponseEntity<Variable[]> response = restTemplate.exchange(uri,
					method, request, Variable[].class);
			int httpStatusCode = response.getStatusCode().value();
			if (httpStatusCode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: "
								+ response.getStatusCode().value());
			}
			return Arrays.asList(response.getBody());
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that adds variable to process instance
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:add-process-instance-variable}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @param variables
	 *            the variables to add
	 * @return The added variables collection
	 */
	@Processor(friendlyName = "Add Variables To Process Instance ")
	@Summary("This processor adds variable to process instance")
	public List<Variable> addProcessInstanceVariable(
			@RestQueryParam("processInstanceId") String processInstanceId,
			@RestQueryParam("variables") Map<String, VariableValueType> variables) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			List<VariableRequest> variableRequests = ConnectorHelper
					.fromMapToVariableRequests(variables);
			HttpEntity<List<VariableRequest>> request = new HttpEntity<List<VariableRequest>>(
					variableRequests, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}/variables");
			URI uri = template.expand(serverUrl, processInstanceId);
			CustomRestTemplate restTemplate = new CustomRestTemplate();
			restTemplate
					.setDefaultResponseContentType(MediaType.APPLICATION_JSON);
			ResponseEntity<Variable[]> response = restTemplate.exchange(uri,
					org.springframework.http.HttpMethod.POST, request,
					Variable[].class);
			return Arrays.asList(response.getBody());
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that gets process instance variable
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-process-instance-variable}
	 * 
	 * @param processInstanceId
	 *            the process instance id
	 * @param variableName
	 *            the process instance variable name
	 * @return The variable object
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Process Instance Variable")
	@Summary("This processor gets a process instance variable")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}/variables/{variableName}", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract Variable getProcessInstanceVariable(
			@RestUriParam("processInstanceId") String processInstanceId,
			@RestUriParam("variableName") String variableName)
			throws IOException;

	/**
	 * Custom processor that updates a process instance variable
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:update-process-instance-variable}
	 * 
	 * @param processInstanceId
	 *            The the process instance id
	 * @param variableName
	 *            the variable name
	 * @param variableType
	 *            the variable name
	 * @param variableValue
	 *            the variable name
	 * @return the updated variable
	 */
	@Processor(friendlyName = "Update Process Instance Variable")
	@Summary("This processor updates a process instance variable")
	public Variable updateProcessInstanceVariable(
			@RestQueryParam("processInstanceId") String processInstanceId,
			@RestQueryParam("variableName") String variableName,
			@RestQueryParam("variableType") String variableType,
			@RestQueryParam("variableValue") String variableValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION_HEADER, authorization);
		headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
		VariableRequest variableRequest = new VariableRequest();
		variableRequest.setName(variableName);
		variableRequest.setType(variableType);
		variableRequest.setValue(variableValue);
		HttpEntity<VariableRequest> request = new HttpEntity<VariableRequest>(
				variableRequest, headers);
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/runtime/process-instances/{processInstanceId}/variables/{variableName}");
		URI uri = template.expand(serverUrl, processInstanceId, variableName);
		RestTemplate restTemplate = new RestTemplate();
		org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.PUT;
		ResponseEntity<Variable> response = restTemplate.exchange(uri, method,
				request, Variable.class);
		Integer statuscode = response.getStatusCode().value();
		if (statuscode != 200) {
			throw new RuntimeException(
					"The request has been failed with code: " + statuscode);
		}
		return response.getBody();
	}

	/**
	 * Custom processor that gets a task by id
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-task-by-id}
	 * 
	 * @param taskId
	 *            the task id
	 * @return The Task
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Processor(friendlyName = "Get Task by id")
	@Summary("This processor gets a task by id")
	public Task getTaskById(@RestQueryParam("taskId") String taskId) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			RestTemplate restTemplate = new RestTemplate();
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}");
			URI uri = template.expand(serverUrl, taskId);
			org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.GET;
			HttpEntity request = new HttpEntity(headers);
			ResponseEntity<Task> response = restTemplate.exchange(uri, method,
					request, Task.class);
			int httpStatusCode = response.getStatusCode().value();
			if (httpStatusCode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: "
								+ response.getStatusCode().value());
			}
			return response.getBody();
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that gets tasks
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-tasks}
	 * 
	 * @param aname
	 *            Task name
	 * @param nameLike
	 *            Task name like
	 * @param description
	 *            Task description
	 * @param priority
	 *            Task priority
	 * @param minimumPriority
	 *            Task minimum priority
	 * @param maximumPriority
	 *            Task maximum priority
	 * @param assignee
	 *            Task assignee
	 * @param assigneeLike
	 *            Task assignee like
	 * @param owner
	 *            Task owner
	 * @param ownerLike
	 *            Task owner like
	 * @param unassigned
	 *            Task unassigned
	 * @param delegationState
	 *            task delegation State
	 * @param candidateUser
	 *            Task candidate User
	 * @param candidateGroup
	 *            Task candidate group groups
	 * @param candidateGroups
	 *            Task candidate
	 * @param involvedUser
	 *            Task involved user
	 * @param taskDefinitionKey
	 *            Task Definition Key
	 * @param taskDefinitionKeyLike
	 *            Task Definition Key like
	 * @param processInstanceId
	 *            Task process instance
	 * @param processInstanceBusinessKey
	 *            Task process Instance Business Key
	 * @param processInstanceBusinessKeyLike
	 *            Task process Instance Business Key Like
	 * @param processDefinitionKey
	 *            process Definition Key
	 * @param processDefinitionKeyLike
	 *            proces DefinitionKeyL ike
	 * @param processDefinitionName
	 *            process Definition Name
	 * @param processDefinitionNameLike
	 *            process Definition Name Like
	 * @param executionId
	 *            the execution id
	 * @param createdOn
	 *            created on
	 * @param createdBefore
	 *            created Before
	 * @param createdAfter
	 *            created After
	 * @param dueOn
	 *            due on
	 * @param dueBefore
	 *            due before
	 * @param dueAfter
	 *            due after
	 * @param withoutDueDate
	 *            without Due Date
	 * @param excludeSubTasks
	 *            exclude Sub Tasks
	 * @param active
	 *            active task
	 * @param includeTaskLocalVariables
	 *            include Task Local Variables
	 * @param includeProcessVariables
	 *            include Process Variables
	 * @param tenantId
	 *            the tenant id
	 * @param tenantIdLike
	 *            tenantId Like
	 * @param withoutTenantId
	 *            without TenantId
	 * @param candidateOrAssigned
	 *            candidate Or Assigned
	 * @return TasksWrapper response
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Processor(friendlyName = "Get tasks")
	@Summary("Custom processor that gets tasks")
	public TasksWrapper getTasks(
			@Optional @RestQueryParam("name") String aname,
			@Optional @RestQueryParam("nameLike") String nameLike,
			@Optional @RestQueryParam("description") String description,
			@Optional @RestQueryParam("priority") Integer priority,
			@Optional @RestQueryParam("minimumPriority") Integer minimumPriority,
			@Optional @RestQueryParam("maximumPriority") Integer maximumPriority,
			@Optional @RestQueryParam("assignee") String assignee,
			@Optional @RestQueryParam("assigneeLike") String assigneeLike,
			@Optional @RestQueryParam("owner") String owner,
			@Optional @RestQueryParam("ownerLike") String ownerLike,
			@Optional @RestQueryParam("unassigned") Boolean unassigned,
			@Optional @RestQueryParam("delegationState") String delegationState,
			@Optional @RestQueryParam("candidateUser") String candidateUser,
			@Optional @RestQueryParam("candidateGroup") String candidateGroup,
			@Optional @RestQueryParam("candidateGroups") String candidateGroups,
			@Optional @RestQueryParam("involvedUser") String involvedUser,
			@Optional @RestQueryParam("taskDefinitionKey") String taskDefinitionKey,
			@Optional @RestQueryParam("taskDefinitionKeyLike") String taskDefinitionKeyLike,
			@Optional @RestQueryParam("processInstanceId") String processInstanceId,
			@Optional @RestQueryParam("processInstanceBusinessKey") String processInstanceBusinessKey,
			@Optional @RestQueryParam("processInstanceBusinessKeyLike") String processInstanceBusinessKeyLike,
			@Optional @RestQueryParam("processDefinitionKey") String processDefinitionKey,
			@Optional @RestQueryParam("processDefinitionKeyLike") String processDefinitionKeyLike,
			@Optional @RestQueryParam("processDefinitionName") String processDefinitionName,
			@Optional @RestQueryParam("processDefinitionNameLike") String processDefinitionNameLike,
			@Optional @RestQueryParam("executionId") String executionId,
			@Optional @RestQueryParam("createdOn") Date createdOn,
			@Optional @RestQueryParam("createdBefore") Date createdBefore,
			@Optional @RestQueryParam("createdAfter") Date createdAfter,
			@Optional @RestQueryParam("dueOn") Date dueOn,
			@Optional @RestQueryParam("dueBefore") Date dueBefore,
			@Optional @RestQueryParam("dueAfter") Date dueAfter,
			@Optional @RestQueryParam("withoutDueDate") Boolean withoutDueDate,
			@Optional @RestQueryParam("excludeSubTasks") Boolean excludeSubTasks,
			@Optional @RestQueryParam("active") Boolean active,
			@Optional @RestQueryParam("includeTaskLocalVariables") Boolean includeTaskLocalVariables,
			@Optional @RestQueryParam("includeProcessVariables") Boolean includeProcessVariables,
			@Optional @RestQueryParam("tenantId") String tenantId,
			@Optional @RestQueryParam("tenantIdLike") String tenantIdLike,
			@Optional @RestQueryParam("withoutTenantId") Boolean withoutTenantId,
			@Optional @RestQueryParam("candidateOrAssigned") String candidateOrAssigned) {
		ResponseEntity<TasksWrapper> response = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			HttpEntity request = new HttpEntity(headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks");
			URI uri = template.expand(serverUrl);
			URIBuilder builder = new URIBuilder(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (aname != null)
				params.add(ConnectorHelper.getNameValuePair("name", aname));
			if (nameLike != null)
				params.add(ConnectorHelper.getNameValuePair("nameLike",
						nameLike));
			if (description != null)
				params.add(ConnectorHelper.getNameValuePair("description",
						description));
			if (priority != null)
				params.add(ConnectorHelper.getNameValuePair("priority",
						Integer.toString(priority)));
			if (minimumPriority != null)
				params.add(ConnectorHelper.getNameValuePair("minimumPriority",
						Integer.toString(minimumPriority)));
			if (maximumPriority != null)
				params.add(ConnectorHelper.getNameValuePair("maximumPriority",
						Integer.toString(maximumPriority)));
			if (assignee != null)
				params.add(ConnectorHelper.getNameValuePair("assignee",
						assignee));
			if (assigneeLike != null)
				params.add(ConnectorHelper.getNameValuePair("assigneeLike",
						assigneeLike));
			if (owner != null)
				params.add(ConnectorHelper.getNameValuePair("owner", owner));
			if (ownerLike != null)
				params.add(ConnectorHelper.getNameValuePair("ownerLike",
						ownerLike));
			if (unassigned != null)
				params.add(ConnectorHelper.getNameValuePair("unassigned",
						Boolean.toString(unassigned)));
			if (delegationState != null)
				params.add(ConnectorHelper.getNameValuePair("delegationState",
						delegationState));
			if (candidateUser != null)
				params.add(ConnectorHelper.getNameValuePair("candidateUser",
						candidateUser));
			if (candidateGroup != null)
				params.add(ConnectorHelper.getNameValuePair("candidateGroup",
						candidateGroup));
			if (candidateGroups != null)
				params.add(ConnectorHelper.getNameValuePair("candidateGroups",
						candidateGroups));
			if (taskDefinitionKey != null)
				params.add(ConnectorHelper.getNameValuePair(
						"taskDefinitionKey", taskDefinitionKey));
			if (taskDefinitionKeyLike != null)
				params.add(ConnectorHelper.getNameValuePair(
						"taskDefinitionKeyLike", taskDefinitionKeyLike));
			if (processInstanceId != null)
				params.add(ConnectorHelper.getNameValuePair(
						"processInstanceId", processInstanceId));
			if (processInstanceBusinessKey != null)
				params.add(ConnectorHelper.getNameValuePair(
						"processInstanceBusinessKey",
						processInstanceBusinessKey));
			if (processInstanceBusinessKeyLike != null)
				params.add(ConnectorHelper.getNameValuePair(
						"processInstanceBusinessKeyLike",
						processInstanceBusinessKeyLike));
			if (processDefinitionName != null)
				params.add(ConnectorHelper.getNameValuePair(
						"processDefinitionName", processDefinitionName));
			if (processDefinitionNameLike != null)
				params.add(ConnectorHelper.getNameValuePair(
						"processDefinitionNameLike", processDefinitionNameLike));
			if (processDefinitionKey != null)
				params.add(ConnectorHelper.getNameValuePair(
						"processDefinitionKey", processDefinitionKey));
			if (processDefinitionKeyLike != null)
				params.add(ConnectorHelper.getNameValuePair(
						"processDefinitionKeyLike", processDefinitionKeyLike));
			if (executionId != null)
				params.add(ConnectorHelper.getNameValuePair("executionId",
						executionId));
			if (createdOn != null)
				params.add(ConnectorHelper.getNameValuePair("createdOn",
						createdOn.toString()));
			if (createdBefore != null)
				params.add(ConnectorHelper.getNameValuePair("createdBefore",
						createdBefore.toString()));
			if (dueAfter != null)
				params.add(ConnectorHelper.getNameValuePair("dueAfter",
						dueAfter.toString()));
			if (dueOn != null)
				params.add(ConnectorHelper.getNameValuePair("dueOn",
						dueOn.toString()));
			if (dueBefore != null)
				params.add(ConnectorHelper.getNameValuePair("dueBefore",
						dueBefore.toString()));
			if (dueAfter != null)
				params.add(ConnectorHelper.getNameValuePair("dueAfter",
						dueAfter.toString()));
			if (involvedUser != null)
				params.add(ConnectorHelper.getNameValuePair("involvedUser",
						involvedUser));
			if (withoutDueDate != null)
				params.add(ConnectorHelper.getNameValuePair("withoutDueDate",
						Boolean.toString(withoutDueDate)));
			if (excludeSubTasks != null)
				params.add(ConnectorHelper.getNameValuePair("excludeSubTasks",
						Boolean.toString(excludeSubTasks)));
			if (active != null)
				params.add(ConnectorHelper.getNameValuePair("active",
						Boolean.toString(active)));
			if (includeTaskLocalVariables != null)
				params.add(ConnectorHelper.getNameValuePair(
						"includeTaskLocalVariables",
						Boolean.toString(includeTaskLocalVariables)));
			if (includeProcessVariables != null)
				params.add(ConnectorHelper.getNameValuePair(
						"includeProcessVariables",
						Boolean.toString(includeProcessVariables)));
			if (tenantId != null)
				params.add(ConnectorHelper.getNameValuePair("tenantId",
						tenantId));
			if (tenantIdLike != null)
				params.add(ConnectorHelper.getNameValuePair("tenantIdLike",
						tenantIdLike));
			if (withoutTenantId != null)
				params.add(ConnectorHelper.getNameValuePair("withoutTenantId",
						Boolean.toString(withoutTenantId)));
			if (candidateOrAssigned != null)
				params.add(ConnectorHelper.getNameValuePair(
						"candidateOrAssigned", candidateOrAssigned));
			builder.addParameters(params);
			RestTemplate restTemplate = new RestTemplate();
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.GET;
			response = restTemplate.exchange(builder.build(), httpMthod,
					request, TasksWrapper.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
		return response.getBody();
	}

	/**
	 * Custom processor that updates a process instance variable
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:update-task}
	 * 
	 * @param taskId
	 *            The task id
	 * @param assignee
	 *            the assignee
	 * @param delegationState
	 *            the delagation state
	 * @param description
	 *            the description
	 * @param dueDate
	 *            the due date
	 * @param aname
	 *            the name
	 * @param owner
	 *            the owner
	 * @param parentTaskId
	 *            the parent task id
	 * @param priority
	 *            the task priority
	 * @return the updated task
	 */
	@Processor(friendlyName = "Update Task")
	@Summary("This processor updates a task")
	public Task updateTask(
			@RestQueryParam("taskId") String taskId,
			@Optional @RestQueryParam("assignee") String assignee,
			@Optional @RestQueryParam("delegationState") String delegationState,
			@Optional @RestQueryParam("description") String description,
			@Optional @RestQueryParam("dueDate") Date dueDate,
			@Optional @RestQueryParam("name") String aname,
			@Optional @RestQueryParam("owner") String owner,
			@Optional @RestQueryParam("parentTaskId") String parentTaskId,
			@Optional @RestQueryParam("priority") Integer priority) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			TaskRequest taskRequest = new TaskRequest();
			if (assignee != null)
				taskRequest = taskRequest.withAssignee(assignee);
			if (delegationState != null)
				taskRequest = taskRequest.withDelegationState(delegationState);
			if (dueDate != null)
				taskRequest = taskRequest.withDueDate(ConnectorHelper
						.toISO8601Date(dueDate));
			if (aname != null)
				taskRequest = taskRequest.withName(aname);
			if (owner != null)
				taskRequest = taskRequest.withOwner(owner);
			if (parentTaskId != null)
				taskRequest = taskRequest.withParentTaskId(parentTaskId);
			if (priority != null)
				taskRequest = taskRequest.withPriority(priority);
			if (description != null)
				taskRequest = taskRequest.withDescription(description);
			HttpEntity<TaskRequest> request = new HttpEntity<TaskRequest>(
					taskRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}");
			URI uri = template.expand(serverUrl, taskId);
			RestTemplate restTemplate = new RestTemplate();
			org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.PUT;
			ResponseEntity<Task> response = restTemplate.exchange(uri, method,
					request, Task.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that performs a task action
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:perform-task-action}
	 * 
	 * @param taskId
	 *            The task id
	 * @param action
	 *            to perform (e.g. complete, claim, delegate, resolve)
	 * @param assignee
	 *            the task assignee
	 * @param variables
	 *            the collection of the task variables
	 * @return the Task for which the action was performed
	 */
	@Processor(friendlyName = "Perform Task Action")
	@Summary("This processor performs a task action")
	public String performTaskAction(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("action") String action,
			@RestQueryParam("assignee") String assignee,
			@Optional @RestQueryParam("variables") Map<String, VariableValueType> variables) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			TaskActionRequest taskActionRequest = new TaskActionRequest();
			taskActionRequest = taskActionRequest.withAssignee(assignee);
			taskActionRequest = taskActionRequest.withAction(action);
			if (variables != null && !variables.isEmpty()) {
				List<VariableRequest> variablesRequest = ConnectorHelper
						.fromMapToVariableRequests(variables);
				taskActionRequest = taskActionRequest
						.withVariables(variablesRequest);
			}
			HttpEntity<TaskActionRequest> request = new HttpEntity<TaskActionRequest>(
					taskActionRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}");
			URI uri = template.expand(serverUrl, taskId);
			CustomRestTemplate restTemplate = new CustomRestTemplate();
			restTemplate
					.setDefaultResponseContentType(MediaType.APPLICATION_JSON);
			org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.POST;
			ResponseEntity<String> response = restTemplate.exchange(uri,
					method, request, String.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return String.valueOf(statuscode);
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that deletes a task
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:delete-task}
	 * 
	 * 
	 * @param taskId
	 *            the task id
	 * @param cascadeHistory
	 *            the cascade History
	 * @param deleteReason
	 *            the delete reason
	 * @return the http response status code
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Delete Task")
	@Summary("This processor deletes a task")
	public String deleteTask(@RestUriParam("taskId") String taskId,
			@Optional @RestQueryParam("cascadeHistory") Boolean cascadeHistory,
			@Optional @RestQueryParam("deleteReason") String deleteReason) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		String statusCode = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}?cascadeHistory={cascadeHistory}&deleteReason={deleteReason}");
		URI uri = template.expand(serverUrl, taskId, cascadeHistory,
				deleteReason);
		RestTemplate restTemplate = new RestTemplate();
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.DELETE);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			response = request.execute();
			statusCode = response.getStatusCode().toString();
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
		return statusCode;
	}

	/**
	 * Custom processor that create a task variables
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:create-task-variables}
	 * 
	 * 
	 * @param taskId
	 *            The task id
	 * @param variables
	 *            the task variables map
	 * @return the created variables collection
	 */
	@Processor(friendlyName = "Create Task Variables")
	@Summary("This processor creates task variables")
	public List<Variable> createTaskVariables(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("variables") Map<String, VariableValueType> variables) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			List<VariableRequest> variableRequests = ConnectorHelper
					.fromMapToVariableRequests(variables);
			HttpEntity<List<VariableRequest>> request = new HttpEntity<List<VariableRequest>>(
					variableRequests, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/variables");
			URI uri = template.expand(serverUrl, taskId);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Variable[]> response = restTemplate.exchange(uri,
					org.springframework.http.HttpMethod.POST, request,
					Variable[].class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 201) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return Arrays.asList(response.getBody());
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that returns all task variables
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-task-variables}
	 * 
	 * 
	 * @param taskId
	 *            The task id
	 * @param scope
	 *            the variables scope
	 * @return the created variables collection
	 */
	@SuppressWarnings("unchecked")
	@Processor(friendlyName = "Create Task Variables")
	@Summary("This processor creates task variables")
	public List<Variable> getTaskVariables(
			@RestQueryParam("taskId") String taskId,
			@Optional @RestQueryParam("scope") String scope) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			@SuppressWarnings("rawtypes")
			HttpEntity request = new HttpEntity(headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/variables");
			URI uri = template.expand(serverUrl, taskId);
			URIBuilder builder = new URIBuilder(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (scope != null)
				params.add(ConnectorHelper.getNameValuePair("scope", scope));
			builder.addParameters(params);
			RestTemplate restTemplate = new RestTemplate();
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.GET;
			ResponseEntity<Variable[]> response = restTemplate.exchange(
					builder.build(), httpMthod, request, Variable[].class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return Arrays.asList(response.getBody());
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that returns a task variable with name
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-task-variable-by-name}
	 * 
	 * 
	 * @param taskId
	 *            The task id
	 * @param variableName
	 *            the variable Name
	 * @param scope
	 *            the variables scope
	 * @return the task variable
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Processor(friendlyName = "Get Task Variable By Variable Name")
	@Summary("This processor returns a task variable with variable name")
	public Variable getTaskVariableByName(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("variableName") String variableName,
			@Optional @RestQueryParam("scope") String scope) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			HttpEntity request = new HttpEntity(headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/variables/{variableName}");
			URI uri = template.expand(serverUrl, taskId, variableName);
			URIBuilder builder = new URIBuilder(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (scope != null)
				params.add(ConnectorHelper.getNameValuePair("scope", scope));
			builder.addParameters(params);
			CustomRestTemplate restTemplate = new CustomRestTemplate();
			restTemplate
					.setDefaultResponseContentType(MediaType.APPLICATION_JSON);
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.GET;
			ResponseEntity<Variable> response = restTemplate.exchange(
					builder.build(), httpMthod, request, Variable.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that creates a task binary variable
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:create-task-binary-variable}
	 * 
	 * 
	 * @param taskId
	 *            The task id
	 * @param variableName
	 *            the variable Name
	 * @param scope
	 *            the variable scope
	 * @param type
	 *            the variable type
	 * @param variableFilePath
	 *            the variable Fil ePath
	 * @return the task variable
	 */
	@Processor(friendlyName = "Get Task Variable By Variable Name")
	@Summary("This processor returns a task variable with variable name")
	public Variable createTaskBinaryVariable(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("name") String variableName,
			@Optional @RestQueryParam("scope") String scope,
			@Optional @RestQueryParam("type") String type,
			@RestQueryParam("variableFilePath") String variableFilePath) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add(AUTHORIZATION_HEADER, authorization);
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			File variableFile = new File(variableFilePath);
			Resource resource = new FileSystemResource(variableFile);
			formData.add("file-part", resource);
			formData.add("name", variableName);
			formData.add("scope", scope);
			if (type == null)
				formData.add("type", "binary");
			else
				formData.add("type", type);
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(
					formData, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/variables");
			URI uri = template.expand(serverUrl, taskId);
			CustomRestTemplate restTemplate = new CustomRestTemplate();
			restTemplate
					.setDefaultResponseContentType(MediaType.APPLICATION_JSON);
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.POST;
			ResponseEntity<Variable> response = restTemplate.exchange(uri,
					httpMthod, httpEntity, Variable.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 201) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}

	/**
	 * Custom processor that returns a task variable binary data
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample activiti:get-task-variable-binary-data}
	 *
	 * 
	 * @param taskId
	 *            the task Id
	 * @param variableName
	 *            the variable name
	 * @param scope
	 *            the variable scope
	 * @return The variable binary data
	 */
	@SuppressWarnings("unchecked")
	@Processor(friendlyName = "Get Task Variable Binary Data")
	@Summary("This processor returns the task variable binary data")
	public byte[] getTaskVariableBinaryData(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("variableName") String variableName,
			@Optional @RestQueryParam("scope") String scope) {
		ResponseEntity<byte[]> response;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add(AUTHORIZATION_HEADER, authorization);
			@SuppressWarnings("rawtypes")
			HttpEntity request = new HttpEntity(headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/variables/{variableName}/data");
			URI uri = template.expand(serverUrl, taskId, variableName);
			URIBuilder builder = new URIBuilder(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (scope != null)
				params.add(ConnectorHelper.getNameValuePair("scope", scope));
			builder.addParameters(params);
			CustomRestTemplate restTemplate = new CustomRestTemplate();
			restTemplate
					.setDefaultResponseContentType(MediaType.APPLICATION_OCTET_STREAM);
			org.springframework.http.HttpMethod method = org.springframework.http.HttpMethod.GET;
			response = restTemplate
					.exchange(builder.build(), method, request, byte[].class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
		return response.getBody();
	}
	
	/**
	 * Custom processor that updates a task existing variable
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample activiti:update-task-variable}
	 * 
	 *
	 * @param taskId
	 *            The task id
	 * @param variableName
	 *            The variable Name
	 * @param scope
	 *      	  The variable scope
	 * @param type
	 *      	  The variable type
	 * @param value
	 *            the variables value
	 * @return the updated variable
	 */
	@Processor(friendlyName = "Update Task Variable")
	@Summary("This processor updates a task variable")
	public Variable updateTaskVariable(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("variableName") String variableName,
			@RestQueryParam("scope") String scope,
			@RestQueryParam("type") String type,
			@RestQueryParam("value") String value) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			VariableRequest variableRequest = ConnectorHelper
					.fromPopertiesToVariableRequests(variableName, scope, type, value);
			HttpEntity<VariableRequest> request = new HttpEntity<VariableRequest>(
					variableRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/variables/{variableName}");
			URI uri = template.expand(serverUrl, taskId, variableName);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Variable> response = restTemplate.exchange(uri,
					org.springframework.http.HttpMethod.PUT, request,
					Variable.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}
	
	/**
	 * Custom processor that updates a task existing binary variable
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample activiti:update-task-binary-variable}
	 * 
	 *
	 * @param taskId
	 *            The task id
	 * @param variableName
	 *            The variable Name
	 * @param scope
	 *      	  The variable scope
	 * @param variableFilePath
	 *            the variable serializable value file
	 * @return the updated variable
	 */
	@Processor(friendlyName = "Update Task Variable")
	@Summary("This processor updates a task variable")
	public Variable updateTaskBinaryVariable(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("variableName") String variableName,
			@RestQueryParam("scope") String scope,
			@RestQueryParam("variableFilePath") String variableFilePath) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.add(AUTHORIZATION_HEADER, authorization);
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			File variableFile = new File(variableFilePath);
			Resource resource = new FileSystemResource(variableFile);
			formData.add("file-part", resource);
			formData.add("name", variableName);
			formData.add("scope", scope);
			//type parameter is by default equals to: binary
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(
					formData, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/variables/{variableName}");
			URI uri = template.expand(serverUrl, taskId, variableName);
			CustomRestTemplate restTemplate = new CustomRestTemplate();
			restTemplate
					.setDefaultResponseContentType(MediaType.APPLICATION_JSON);
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.PUT;
			ResponseEntity<Variable> response = restTemplate.exchange(uri,
					httpMthod, httpEntity, Variable.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}
	
	/**
	 * Custom processor that deletes a task existing variable
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample activiti:delete-task-variable}
	 * 
	 *
	 * @param taskId
	 *            The task id
	 * @param variableName
	 *            The variable Name
	 * @param scope
	 *      	  The variable scope
	 * @return the http status code
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Processor(friendlyName = "Delete Task Variable")
	@Summary("This processor deletes a task variable")
	public String deleteTaskVariable(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("variableName") String variableName,
			@Optional @RestQueryParam("scope") String scope) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			HttpEntity httpEntity = new HttpEntity(headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/variables/{variableName}");
			URI uri = template.expand(serverUrl, taskId, variableName);
			URIBuilder builder = new URIBuilder(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (scope != null)
				params.add(ConnectorHelper.getNameValuePair("scope", scope));
			builder.addParameters(params);
			CustomRestTemplate restTemplate = new CustomRestTemplate();
			restTemplate
					.setDefaultResponseContentType(MediaType.APPLICATION_JSON);
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.DELETE;
			ResponseEntity<String> response = restTemplate.exchange(builder.build(),
					httpMthod, httpEntity, String.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 204) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return String.valueOf(statuscode);
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}
	
	/**
	 * Custom processor that deletes a task local variables
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:delete-task-local-variables}
	 * 
	 * @param taskId
	 *            the task id
	 * @return the http status code
	 */
	@Processor(friendlyName = "Delete Task Local Variables")
	@Summary("This processor deletes a task local variables")
	public String deleteTaskLocalVariables(
			@RestUriParam("taskId") String taskId) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		String statusCode = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/variables");
		URI uri = template.expand(serverUrl, taskId);
		RestTemplate restTemplate = new RestTemplate();
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.DELETE);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			response = request.execute();
			statusCode = response.getStatusCode().toString();
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
		return statusCode;
	}

	
	/**
	 * Custom processor that gets task candidate starters
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-task-candidate-starters}
	 * 
	 * @param taskId
	 *            the task id
	 * @return CandidateStartersWrapper response
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Get Task Candidate Starters")
	@Summary("This processor gets a task candidate starters")
	@RestCall(uri = "{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/identitylinks", method = HttpMethod.GET, contentType = "application/json", exceptions = { @RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]") })
	public abstract CandidateStartersWrapper getTaskCandidateStarters(
			@RestUriParam("taskId") String taskId)
			throws IOException;
	
	/**
	 * Custom processor that adds a candidate starter to a task
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:add-candidate-starter-to-task}
	 * 
	 * @param taskId
	 *            the task id
	 * @param user
	 *            the candidate starter user
	 * @param groupId
	 *            the candidate starter groupId
	 * @param type
	 *            the candidate starter type
	 * @return the candidate starter response
	 */
	@Processor(friendlyName = "Add Candidate Starter To Task")
	@Summary("This processor adds a candidate starter to a task")
	public CandidateStarter addCandidateStarterToTask(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("user") String user,
		    @RestQueryParam("groupId") String groupId,
		    @RestQueryParam("type") String type) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			CandidateStarterRequest candidateStarterRequest = new CandidateStarterRequest();
			candidateStarterRequest = candidateStarterRequest.withUser(user);
			candidateStarterRequest = candidateStarterRequest.withGroupId(groupId);
			candidateStarterRequest = candidateStarterRequest.withType(type);
			HttpEntity<CandidateStarterRequest> request = new HttpEntity<CandidateStarterRequest>(
					candidateStarterRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/identitylinks");
			URI uri = template.expand(serverUrl, taskId);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<CandidateStarter> response = restTemplate
					.postForEntity(uri, request, CandidateStarter.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 201) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
		}
	}
	

	/**
	 * Custom processor that deletes a task candidate starter
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:delete-task-candidate-starter}
	 * 
	 * @param taskId
	 *            Task id
	 * @param family
	 *            identity link family
	 * @param identityId
	 *            identity id
	 * @param type
	 *            type
	 * @return String containing the deletion status code
	 * @throws IOException
	 *             exception thrown
	 */
	@Processor(friendlyName = "Delete Task Candidate Starter (Identity Link)")
	@Summary("This processor deletes a Task Candidate Starter (Identity Link)")
	public String deleteTaskCandidateStarter(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("family") String family,
			@RestQueryParam("identityId") String identityId,
			@RestQueryParam("type") String type) {
		ClientHttpRequest request = null;
		ClientHttpResponse response = null;
		String statusCode = null;
		UriTemplate template = new UriTemplate(
				"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/identitylinks/{family}/{identityId}/{type}");
		URI uri = template.expand(serverUrl, taskId, family, identityId, type);
		RestTemplate restTemplate = new RestTemplate();
		try {
			request = restTemplate.getRequestFactory().createRequest(uri,
					org.springframework.http.HttpMethod.DELETE);
			request.getHeaders().add(AUTHORIZATION_HEADER, authorization);
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			response = request.execute();
			statusCode = response.getStatusCode().toString();
		} catch (IOException e) {
			throw new MuleRuntimeException(e);
		}
		return statusCode;
	}
	
	/**
	 * Custom processor that gets task groups or users candidate starters
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:get-task-candidate-starters-by-family}
	 * 
	 * @param taskId
	 *            the task id
	 * @param family
	 *            the candidate starter (identity link) family
	 * @return Candidate Starters collection 
	 */
	@SuppressWarnings("unchecked")
	@Processor(friendlyName = "Get Task Candidate Starters by family {users or groups}")
	@Summary("This processor gets a task candidate starters by family")
	public List<CandidateStarter> getTaskCandidateStartersByFamily(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("family") String family) {
		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			@SuppressWarnings("rawtypes")
			HttpEntity request = new HttpEntity(headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/identitylinks/{family}");
			URI uri = template.expand(serverUrl, taskId, family);
			RestTemplate restTemplate = new RestTemplate();
			org.springframework.http.HttpMethod httpMthod = org.springframework.http.HttpMethod.GET;
			ResponseEntity<CandidateStarter[]> response = restTemplate.exchange(
					uri, httpMthod, request, CandidateStarter[].class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 200) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return Arrays.asList(response.getBody());
		} catch (Exception e) {
			throw new MuleRuntimeException(e);
		}
	}
	
	/**
	 * Custom processor that creates a task comment
	 * 
	 * {@sample.xml ../../../doc/activiti-connector.xml.sample
	 * activiti:create-task-comment}
	 * 
	 * @param taskId
	 *            the task id
	 * @param message
	 *            the comment message
	 * @param saveProcessInstanceId
	 *            whether or not to save message to process instance id 
	 * @return the task comment
	 */
	@Processor(friendlyName = "Create Task Comment")
	@Summary("This processor creates a task comment")
	public Comment createTaskComment(
			@RestQueryParam("taskId") String taskId,
			@RestQueryParam("message") String message,
		    @RestQueryParam("saveProcessInstanceId") Boolean saveProcessInstanceId) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add(AUTHORIZATION_HEADER, authorization);
			headers.add(ACCEPT_HEADER, MediaType.APPLICATION_JSON.toString());
			CommentRequest commentRequest = new CommentRequest();
			commentRequest = commentRequest.withMessage(message);
			if(saveProcessInstanceId != null) commentRequest = commentRequest.withSaveProcessInstanceId(saveProcessInstanceId);
			HttpEntity<CommentRequest> request = new HttpEntity<CommentRequest>(
					commentRequest, headers);
			UriTemplate template = new UriTemplate(
					"{serverUrl}/activiti-rest/service/runtime/tasks/{taskId}/comments");
			URI uri = template.expand(serverUrl, taskId);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Comment> response = restTemplate
					.postForEntity(uri, request, Comment.class);
			Integer statuscode = response.getStatusCode().value();
			if (statuscode != 201) {
				throw new RuntimeException(
						"The request has been failed with code: " + statuscode);
			}
			return response.getBody();
		} catch (RestClientException e) {
			throw new MuleRuntimeException(e);
		}
	}
	
	

	/**
	 * Connect
	 * 
	 * @param username
	 *            A username
	 * @param password
	 *            A password
	 * @throws ConnectionException
	 */
	@Connect
	public void connect(@ConnectionKey String username,
			@Password String password) throws ConnectionException {
		try {
			ConnectorHelper.validateCredentials(username, password);
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			URI uri = new URI(serverUrl);
			AuthScope authscp = new AuthScope(uri.getHost(), uri.getPort());
			credentialsProvider.setCredentials(authscp,
					new UsernamePasswordCredentials(username, password));
			HttpClientContext localContext = HttpClientContext.create();
			localContext.setCredentialsProvider(credentialsProvider);
			connection = localContext.getConnection();
			authorization = ConnectorHelper.setBasicAuthorization(username,
					password);
			System.setProperty(ALLOW_ENCODED_SLASH_PROPERTY, "true");
		} catch (Exception e) {
			throw new ConnectionException(ConnectionExceptionCode.UNKNOWN,
					null, e.getMessage(), e);
		}
	}

	/**
	 * Disconnect
	 * 
	 * @throws IOException
	 */
	@Disconnect
	public void disconnect() throws IOException {
		connection.close();
	}

	/**
	 * Are we connected
	 */
	@ValidateConnection
	public boolean isConnected() {
		if (connection != null && connection.isOpen()) {
			return true;
		}
		return false;
	}

	/**
	 * Are we connected
	 */
	@ConnectionIdentifier
	public String connectionId() {
		return "001";
	}

	/**
	 * Gets the authorization
	 * 
	 * @return authorization
	 */
	public String getAuthorization() {
		return authorization;
	}

	/**
	 * Sets the authorization
	 * 
	 * @param authorization
	 */
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	/**
	 * Gets the Activiti server url
	 * 
	 * @return serverUri
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Sets the Activiti server url
	 * 
	 * @param serverUri
	 *            the server url
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

}