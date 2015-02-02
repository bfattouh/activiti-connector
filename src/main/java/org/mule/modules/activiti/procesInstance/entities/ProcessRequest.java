/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.procesInstance.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.mule.modules.activiti.variable.entities.Variable;

/**
 * 
 * @author Bouchaib Fattouh - Appnovation Technologies
 * 
 */

@JsonPropertyOrder({ "processDefinitionKey", "processDefinitionId", "message",
		"businessKey", "tenantId", "variables" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProcessRequest {

	@JsonProperty("processDefinitionKey")
	private String processDefinitionKey;

	@JsonProperty("processDefinitionId")
	private String processDefinitionId;

	@JsonProperty("message")
	private String message;

	@JsonProperty("businessKey")
	private String businessKey;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("variables")
	private List<Variable> variables = new ArrayList<Variable>();

	public ProcessRequest() {
	}

	public ProcessRequest withProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
		return this;
	}

	public ProcessRequest withProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
		return this;
	}

	public ProcessRequest withMessage(String message) {
		this.message = message;
		return this;
	}

	public ProcessRequest withBusinessKey(String businessKey) {
		this.businessKey = businessKey;
		return this;
	}

	public ProcessRequest withTenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public ProcessRequest withVariables(Map<String, String> variables) {
		if (variables != null && !variables.isEmpty()) {
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
	 * @param processDefinitionId
	 *            The processDefinitionId
	 */
	@JsonProperty("processDefinitionId")
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
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
	 * @param tenantId
	 *            The tenantId
	 */
	@JsonProperty("tenantId")
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * 
	 * @return The message
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 *            The message
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
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

}
