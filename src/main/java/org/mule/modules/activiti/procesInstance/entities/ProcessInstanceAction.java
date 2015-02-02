/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.procesInstance.entities;

import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;


@JsonPropertyOrder({ "action" })
public class ProcessInstanceAction {

	@JsonProperty("action")
	private String action;

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

	public ProcessInstanceAction withAction(String action) {
		this.action = action;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public static Object unMarshalJSON(Class<ProcessInstanceAction> iClass, String payload)
			throws Exception {
		StringReader reader = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			reader = new StringReader(payload);
			Object obj = mapper.readValue(reader, iClass);
			return obj;
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				IOUtils.closeQuietly(reader);
			}
		}
	}
	
	public static String marshalJSON(Class<ProcessInstanceAction> iClass, Object payload) throws Exception {			
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}

}
