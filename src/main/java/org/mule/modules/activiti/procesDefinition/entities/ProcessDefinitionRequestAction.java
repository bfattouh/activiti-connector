/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.procesDefinition.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "action", "includeProcessInstances", "date" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessDefinitionRequestAction {

	@JsonProperty("action")
	private String action;
	@JsonProperty("includeProcessInstances")
	private Boolean includeProcessInstances;
	@JsonProperty("date")
	private String date;

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

	public ProcessDefinitionRequestAction withAction(String action) {
		this.action = action;
		return this;
	}

	/**
	 * 
	 * @return The includeProcessInstances
	 */
	@JsonProperty("includeProcessInstances")
	public Boolean getIncludeProcessInstances() {
		return includeProcessInstances;
	}

	/**
	 * 
	 * @param includeProcessInstances
	 *            The includeProcessInstances
	 */
	@JsonProperty("includeProcessInstances")
	public void setIncludeProcessInstances(Boolean includeProcessInstances) {
		this.includeProcessInstances = includeProcessInstances;
	}

	public ProcessDefinitionRequestAction withIncludeProcessInstances(
			Boolean includeProcessInstances) {
		this.includeProcessInstances = includeProcessInstances;
		return this;
	}

	/**
	 * 
	 * @return The date
	 */
	@JsonProperty("date")
	public String getDate() {
		return date;
	}

	/**
	 * 
	 * @param date
	 *            The date
	 */
	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	public ProcessDefinitionRequestAction withDate(String date) {
		this.date = date;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
