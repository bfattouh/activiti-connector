/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.task.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonRawValue;

@JsonPropertyOrder({ "assignee", "delegationState", "description", "dueDate",
		"name", "owner", "parentTaskId", "priority" })
public class TaskRequest {

	@JsonProperty("assignee")
	private String assignee;
	@JsonProperty("delegationState")
	private String delegationState;
	@JsonProperty("description")
	private String description;
	@JsonProperty("dueDate")
	private String dueDate;
	@JsonProperty("name")
	private String name;
	@JsonProperty("owner")
	private String owner;
	@JsonProperty("parentTaskId")
	private String parentTaskId;
	@JsonProperty("priority")
	@JsonRawValue
	private Integer priority;

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

	public TaskRequest withAssignee(String assignee) {
		this.assignee = assignee;
		return this;
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

	public TaskRequest withDelegationState(String delegationState) {
		this.delegationState = delegationState;
		return this;
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

	public TaskRequest withDescription(String description) {
		this.description = description;
		return this;
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

	public TaskRequest withDueDate(String dueDate) {
		this.dueDate = dueDate;
		return this;
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

	public TaskRequest withName(String name) {
		this.name = name;
		return this;
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

	public TaskRequest withOwner(String owner) {
		this.owner = owner;
		return this;
	}

	/**
	 * 
	 * @return The parentTaskId
	 */
	@JsonProperty("parentTaskId")
	public String getParentTaskId() {
		return parentTaskId;
	}

	/**
	 * 
	 * @param parentTaskId
	 *            The parentTaskId
	 */
	@JsonProperty("parentTaskId")
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public TaskRequest withParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
		return this;
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

	public TaskRequest withPriority(Integer priority) {
		this.priority = priority;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


}