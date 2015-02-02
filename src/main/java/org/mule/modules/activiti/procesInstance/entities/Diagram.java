/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package org.mule.modules.activiti.procesInstance.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "id", "url", "businessKey", "suspended",
		"processDefinitionUrl", "activityId" })
public class Diagram {

	@JsonProperty("id")
	private String id;
	@JsonProperty("url")
	private String url;
	@JsonProperty("businessKey")
	private String businessKey;
	@JsonProperty("suspended")
	private boolean suspended;
	@JsonProperty("processDefinitionUrl")
	private String processDefinitionUrl;
	@JsonProperty("activityId")
	private String activityId;

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
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public Diagram withId(String id) {
		this.id = id;
		return this;
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
	 * @param url
	 *            The url
	 */
	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	public Diagram withUrl(String url) {
		this.url = url;
		return this;
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
	 * @param businessKey
	 *            The businessKey
	 */
	@JsonProperty("businessKey")
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Diagram withBusinessKey(String businessKey) {
		this.businessKey = businessKey;
		return this;
	}

	/**
	 * 
	 * @return The suspended
	 */
	@JsonProperty("suspended")
	public boolean isSuspended() {
		return suspended;
	}

	/**
	 * 
	 * @param suspended
	 *            The suspended
	 */
	@JsonProperty("suspended")
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public Diagram withSuspended(boolean suspended) {
		this.suspended = suspended;
		return this;
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
	 * @param processDefinitionUrl
	 *            The processDefinitionUrl
	 */
	@JsonProperty("processDefinitionUrl")
	public void setProcessDefinitionUrl(String processDefinitionUrl) {
		this.processDefinitionUrl = processDefinitionUrl;
	}

	public Diagram withProcessDefinitionUrl(String processDefinitionUrl) {
		this.processDefinitionUrl = processDefinitionUrl;
		return this;
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
	 * @param activityId
	 *            The activityId
	 */
	@JsonProperty("activityId")
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Diagram withActivityId(String activityId) {
		this.activityId = activityId;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
