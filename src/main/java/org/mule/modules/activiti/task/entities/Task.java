/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.activiti.task.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonPropertyOrder({ "assignee", "createTime", "delegationState",
		"description", "dueDate", "execution", "id", "name", "owner",
		"parentTask", "priority", "processDefinition", "processInstance",
		"suspended", "taskDefinitionKey", "url", "tenantId" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Task {

	@JsonProperty("assignee")
	private String assignee;
	@JsonProperty("createTime")
	private String createTime;
	@JsonProperty("delegationState")
	private String delegationState;
	@JsonProperty("description")
	private String description;
	@JsonProperty("dueDate")
	private String dueDate;
	@JsonProperty("execution")
	private String execution;
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("owner")
	private String owner;
	@JsonProperty("parentTask")
	private String parentTask;
	@JsonProperty("priority")
	private Integer priority;
	@JsonProperty("processDefinition")
	private String processDefinition;
	@JsonProperty("processInstance")
	private String processInstance;
	@JsonProperty("suspended")
	private Boolean suspended;
	@JsonProperty("taskDefinitionKey")
	private String taskDefinitionKey;
	@JsonProperty("url")
	private String url;
	@JsonProperty("tenantId")
	private Object tenantId;

	/**
	 * 
	 * @return The assignee
	 */
	@JsonProperty("assignee")
	public String getAssignee() {
		return assignee;
	}

	/**
	 * 
	 * @param assignee
	 *            The assignee
	 */
	@JsonProperty("assignee")
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	/**
	 * 
	 * @return The createTime
	 */
	@JsonProperty("createTime")
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * 
	 * @param createTime
	 *            The createTime
	 */
	@JsonProperty("createTime")
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 
	 * @return The delegationState
	 */
	@JsonProperty("delegationState")
	public String getDelegationState() {
		return delegationState;
	}

	/**
	 * 
	 * @param delegationState
	 *            The delegationState
	 */
	@JsonProperty("delegationState")
	public void setDelegationState(String delegationState) {
		this.delegationState = delegationState;
	}

	/**
	 * 
	 * @return The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return The dueDate
	 */
	@JsonProperty("dueDate")
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * 
	 * @param dueDate
	 *            The dueDate
	 */
	@JsonProperty("dueDate")
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * 
	 * @return The execution
	 */
	@JsonProperty("execution")
	public String getExecution() {
		return execution;
	}

	/**
	 * 
	 * @param execution
	 *            The execution
	 */
	@JsonProperty("execution")
	public void setExecution(String execution) {
		this.execution = execution;
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
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The owner
	 */
	@JsonProperty("owner")
	public String getOwner() {
		return owner;
	}

	/**
	 * 
	 * @param owner
	 *            The owner
	 */
	@JsonProperty("owner")
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * 
	 * @return The parentTask
	 */
	@JsonProperty("parentTask")
	public String getParentTask() {
		return parentTask;
	}

	/**
	 * 
	 * @param parentTask
	 *            The parentTask
	 */
	@JsonProperty("parentTask")
	public void setParentTask(String parentTask) {
		this.parentTask = parentTask;
	}

	/**
	 * 
	 * @return The priority
	 */
	@JsonProperty("priority")
	public Integer getPriority() {
		return priority;
	}

	/**
	 * 
	 * @param priority
	 *            The priority
	 */
	@JsonProperty("priority")
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * 
	 * @return The processDefinition
	 */
	@JsonProperty("processDefinition")
	public String getProcessDefinition() {
		return processDefinition;
	}

	/**
	 * 
	 * @param processDefinition
	 *            The processDefinition
	 */
	@JsonProperty("processDefinition")
	public void setProcessDefinition(String processDefinition) {
		this.processDefinition = processDefinition;
	}

	/**
	 * 
	 * @return The processInstance
	 */
	@JsonProperty("processInstance")
	public String getProcessInstance() {
		return processInstance;
	}

	/**
	 * 
	 * @param processInstance
	 *            The processInstance
	 */
	@JsonProperty("processInstance")
	public void setProcessInstance(String processInstance) {
		this.processInstance = processInstance;
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
	 * @param suspended
	 *            The suspended
	 */
	@JsonProperty("suspended")
	public void setSuspended(Boolean suspended) {
		this.suspended = suspended;
	}

	/**
	 * 
	 * @return The taskDefinitionKey
	 */
	@JsonProperty("taskDefinitionKey")
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	/**
	 * 
	 * @param taskDefinitionKey
	 *            The taskDefinitionKey
	 */
	@JsonProperty("taskDefinitionKey")
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
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

	/**
	 * 
	 * @return The tenantId
	 */
	@JsonProperty("tenantId")
	public Object getTenantId() {
		return tenantId;
	}

	/**
	 * 
	 * @param tenantId
	 *            The tenantId
	 */
	@JsonProperty("tenantId")
	public void setTenantId(Object tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
