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
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonPropertyOrder({ "variables" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VariablesWrapper {

	@JsonProperty("variables")
	private List<Variable> variables = new ArrayList<Variable>();

	/**
	 * 
	 * @return The variablesRequests
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

}
