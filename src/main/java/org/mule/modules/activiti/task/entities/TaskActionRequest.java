/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.task.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.mule.modules.activiti.variable.entities.VariableRequest;

/**
 * 
 * @author bfattouh
 * 
 */
@JsonPropertyOrder({ "action", "assignee", "variablesRequest" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TaskActionRequest {

	@JsonProperty("action")
	private String action;
	@JsonProperty("assignee")
	private String assignee;
	@JsonProperty("variables")
	private List<VariableRequest> variablesRequest = new ArrayList<VariableRequest>();

	/**
	 * 
	 * @return The action
	 */
	@JsonProperty("action")
	public String getAction() {
		return action;
	}

	/**
	 * 
	 * @param action
	 *            The action
	 */
	@JsonProperty("action")
	public void setAction(String action) {
		this.action = action;
	}

	public TaskActionRequest withAction(String action) {
		this.action = action;
		return this;
	}

	/**
	 * 
	 * @return The variablesRequest
	 */
	@JsonProperty("variables")
	public List<VariableRequest> getVariablesRequest() {
		return variablesRequest;
	}

	/**
	 * 
	 * @param variablesRequest
	 *            The variablesRequest
	 */
	@JsonProperty("variablesRequest")
	public void setVariablesRequest(List<VariableRequest> variablesRequest) {
		this.variablesRequest = variablesRequest;
	}

	public TaskActionRequest withVariables(
			List<VariableRequest> variablesRequest) {
		this.variablesRequest = variablesRequest;
		return this;
	}

	/**
	 * 
	 * @return assignee
	 */
	@JsonProperty("assignee")
	public String getAssignee() {
		return assignee;
	}

	/**
	 * 
	 * @param assignee
	 */
	@JsonProperty("assignee")
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public TaskActionRequest withAssignee(String assignee) {
		this.assignee = assignee;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
