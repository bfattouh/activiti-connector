/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.variable.entities;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "variableRequests" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class VariableRequestsWrapper {

	@JsonProperty("variableRequests")
	private List<VariableRequest> variableRequests = new ArrayList<VariableRequest>();

	/**
	 * 
	 * @return The variablesRequests
	 */
	@JsonProperty("variableRequests")
	public List<VariableRequest> getVariableRequest() {
		return variableRequests;
	}

	/**
	 * 
	 * @param variablesRequests
	 *            The variablesRequests
	 */
	@JsonProperty("variableRequests")
	public void setVariableRequests(List<VariableRequest> variableRequests) {
		this.variableRequests = variableRequests;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
