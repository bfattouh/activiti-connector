/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.procesInstance.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.mule.modules.activiti.variable.entities.Variable;

@JsonPropertyOrder({ "processDefinitionKey", "variables" })
public class ProcessInstanceQuery {

	@JsonProperty("processDefinitionKey")
	private String processDefinitionKey;
	@JsonProperty("variables")
	private List<Variable> variables = new ArrayList<Variable>();

	/**
	 * 
	 * @return The processDefinitionKey
	 */
	@JsonProperty("processDefinitionKey")
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	/**
	 * 
	 * @param processDefinitionKey
	 *            The processDefinitionKey
	 */
	@JsonProperty("processDefinitionKey")
	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public ProcessInstanceQuery withProcessDefinitionKey(
			String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
		return this;
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

	public ProcessInstanceQuery withVariables(List<Variable> variables) {
		this.variables = variables;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public ProcessInstanceQuery withVariables(Map<String, String> variables) {
    	if(variables!=null && !variables.isEmpty()){
    		List<Variable> vars = new ArrayList<Variable>();
    		Variable variable;
    		for (String key : variables.keySet()) {
				variable = new Variable();
				variable.setName(key);
				variable.setValue(variables.get(key));
				vars.add(variable);
			}
    		this.variables = vars;
    	}
		return this;
	}

}
