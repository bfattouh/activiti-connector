/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package org.mule.modules.activiti.procesInstance.entities;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.mule.modules.activiti.variable.entities.Variable;


@JsonPropertyOrder({"id", "url", "processDefinitionId", "businessKey", "suspended", "processDefinitionUrl", "activityId",
		"tenantId", "ended", "completed", "variables"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessInstance {

	@JsonProperty("id")
	private String id;
	@JsonProperty("url")
	private String url;
	@JsonProperty("processDefinitionId")
	private String processDefinitionId;
	@JsonProperty("businessKey")
	private String businessKey;
	@JsonProperty("suspended")
	private Boolean suspended;
	@JsonProperty("processDefinitionUrl")
	private String processDefinitionUrl;
	@JsonProperty("activityId")
	private String activityId;
	@JsonProperty("tenantId")
	private String tenantId;
	@JsonProperty("ended")
	private Boolean ended;
	@JsonProperty("completed")
	private Boolean completed;
	@JsonProperty("variables")
	private List<Variable> variables = new ArrayList<Variable>();
	
	public ProcessInstance(){
	}
		
	
	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * @param The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The url
	 */
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}


	/**
	 * 
	 * @param The url
	 */
	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * 
	 * @return The businessKey
	 */
	@JsonProperty("businessKey")
	public String getBusinessKey() {
		return businessKey;
	}


	/**
	 * 
	 * @param The businessKey
	 */
	@JsonProperty("businessKey")
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}



	/**
	 * 
	 * @return The suspended
	 */
	@JsonProperty("suspended")
	public Boolean getSuspended() {
		return suspended;
	}


	/**
	 * 
	 * @param The suspended
	 */
	@JsonProperty("suspended")
	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}



	/**
	 * 
	 * @return The processDefinitionUrl
	 */
	@JsonProperty("processDefinitionUrl")
	public String getProcessDefinitionUrl() {
		return processDefinitionUrl;
	}



	/**
	 * 
	 * @param The processDefinitionUrl
	 */
	@JsonProperty("processDefinitionUrl")
	public void setProcessDefinitionUrl(String processDefinitionUrl) {
		this.processDefinitionUrl = processDefinitionUrl;
	}


	/**
	 * 
	 * @return The activityId
	 */
	@JsonProperty("activityId")
	public String getActivityId() {
		return activityId;
	}


	/**
	 * 
	 * @param The activityId
	 */
	@JsonProperty("activityId")
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}


	/**
	 * 
	 * @return The tenantId
	 */
	@JsonProperty("tenantId")
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * 
	 * @param The tenantId
	 */
	@JsonProperty("tenantId")
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	/**
	 * 
	 * @return The ended
	 */
	@JsonProperty("ended")
	public Boolean getEnded() {
		return ended;
	}

	/**
	 * 
	 * @param The ended
	 */
	@JsonProperty("ended")
	public void setEnded(Boolean ended) {
		this.ended = ended;
	}

	/**
	 * 
	 * @return The processDefinitionId
	 */
	@JsonProperty("processDefinitionId")
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	/**
	 * 
	 * @param The processDefinitionId
	 */
	@JsonProperty("processDefinitionId")
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	
	/**
	 * 
	 * @return The completed
	 */
	@JsonProperty("completed")
	public Boolean getCompleted() {
		return completed;
	}

	/**
	 * 
	 * @param The completed
	 */
	@JsonProperty("completed")
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}


	/**
	 * 
	 * @return The variables
	 */
	@JsonProperty("variables")
	public List<Variable> getVariables() {
		return variables;
	}

	/**
	 * 
	 * @param variables
	 *            The variables
	 */
	@JsonProperty("variables")
	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public static Object unMarshalJSON(Class<ProcessInstance> iClass, String payload)
			throws Exception {
		StringReader reader = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			reader = new StringReader(payload);
			Object obj = mapper.readValue(reader, iClass);
			return obj;
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				IOUtils.closeQuietly(reader);
			}
		}
	}
	
	public static String marshalJSON(Class<ProcessInstance> iClass, Object payload) throws Exception {			
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}



}
