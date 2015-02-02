/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.variable.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author Bouchaib Fattouh - Appnovation Technologies
 * 
 */
@JsonPropertyOrder({ "name", "type", "value" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VariableRequest {

	@JsonProperty("name")
	private String name;

	@JsonProperty("type")
	private String type;

	@JsonIgnore(value = true)
	private Object value;

	@JsonProperty("value")
	private VariableValueEntry valueEntry = new VariableValueEntry(type, value);

	public VariableRequest() {
	}

	public VariableRequest(String name, String type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.valueEntry = new VariableValueEntry(type, value);
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
	 * @return
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore(value = true)
	public Object getValue() {
		return value;
	}

	/**
	 * 
	 * @param type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @param value
	 */
	@JsonIgnore(value = true)
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 
	 * @return valueEntry
	 */
	@JsonProperty("value")
	public VariableValueEntry getValueEntry() {
		return valueEntry;
	}

	/**
	 * 
	 * @param valueEntry
	 */
	@JsonProperty("value")
	public void setValueEntry(VariableValueEntry valueEntry) {
		this.valueEntry = valueEntry;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
