/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.activiti.variable.entities;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public class VariableValueEntry implements JsonSerializableWithType {

	private Object value;
	private String type;

	public VariableValueEntry(String type, Object value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public void serialize(JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		if (!type.equals("string") && !type.equals("date")) {
			jgen.writeRawValue(value.toString());
		} else {
			jgen.writeString((String) value);
		}
	}

	@Override
	public void serializeWithType(JsonGenerator jgen,
			SerializerProvider provider, TypeSerializer typeSer)
			throws IOException, JsonProcessingException {
		this.serialize(jgen, provider);
	}

}
