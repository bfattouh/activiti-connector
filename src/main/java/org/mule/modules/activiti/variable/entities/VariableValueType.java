/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.variable.entities;

public class VariableValueType {

	private Object value;

	private String type;

	public VariableValueType(String type, Object value) {
		this.value = value;
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public String getType() {
		return type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public VariableValueType withType(String type) {
		this.type = type;
		return this;
	}

	public VariableValueType withValue(Object value) {
		this.value = value;
		return this;
	}

}
