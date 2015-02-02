/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.variable.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author Bouchaib Fattouh - Appnovation Technologies
 * 
 */
@JsonPropertyOrder({ "name", "value", "operation", "type", "scope", "valueUrl" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Variable {

	@JsonProperty("name")
	private String name;

	@JsonProperty("value")
	private String value;

	@JsonProperty("operation")
	private String operation;

	@JsonProperty("type")
	private String type;
	
	@JsonProperty("scope")
	private String scope;
	
	@JsonProperty("valueUrl")
	private String valueUrl;
	
	public Variable withName(String name) {
		this.name = name;
		return this;
	}
	
	public Variable withType(String type) {
		this.type = type;
		return this;
	}
	
	public Variable withValue(String value) {
		this.value = value;
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

	/**
	 * 
	 * @return The value
	 */
	@JsonProperty("value")
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 *            The value
	 */
	@JsonProperty("value")
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return The operation
	 */
	@JsonProperty("operation")
	public String getOperation() {
		return operation;
	}

	/**
	 * 
	 * @param operation
	 *            The operation
	 */
	@JsonProperty("operation")
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * 
	 * @return The type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            The type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return the scope
	 */
	@JsonProperty("scope")
	public String getScope() {
		return scope;
	}
	
	/**
	 * 
	 * @param scope the scope
	 */
	@JsonProperty("scope")
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	/**
	 * 
	 * @return the valueUrl;
	 */
	@JsonProperty("valueUrl")
	public String getValueUrl() {
		return valueUrl;
	}
	
	/**
	 * 
	 * @param valueUrl the valueUrl
	 */
	@JsonProperty("valueUrl")
	public void setValueUrl(String valueUrl) {
		this.valueUrl = valueUrl;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	



}
