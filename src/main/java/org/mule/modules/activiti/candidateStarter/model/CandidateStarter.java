/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.candidateStarter.model;

import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;



@JsonPropertyOrder({ "url", "user", "group", "type" })
@JsonIgnoreProperties(ignoreUnknown=true)
public class CandidateStarter {

	@JsonProperty("url")
	private String url;
	@JsonProperty("user")
	private String user;
	@JsonProperty("group")
	private Object group;
	@JsonProperty("type")
	private String type;

	/**
	 * 
	 * @return The url
	 */
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 *            The url
	 */
	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	public CandidateStarter withUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 
	 * @return The user
	 */
	@JsonProperty("user")
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            The user
	 */
	@JsonProperty("user")
	public void setUser(String user) {
		this.user = user;
	}

	public CandidateStarter withUser(String user) {
		this.user = user;
		return this;
	}

	/**
	 * 
	 * @return The group
	 */
	@JsonProperty("group")
	public Object getGroup() {
		return group;
	}

	/**
	 * 
	 * @param group
	 *            The group
	 */
	@JsonProperty("group")
	public void setGroup(Object group) {
		this.group = group;
	}

	public CandidateStarter withGroup(Object group) {
		this.group = group;
		return this;
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

	public CandidateStarter withType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	public static List<CandidateStarter> unMarshalJSON(Class<List<CandidateStarter>> iClass, String payload)
			throws Exception {
		StringReader reader = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			reader = new StringReader(payload);
			List<CandidateStarter> obj = mapper.readValue(reader, new TypeReference<List<CandidateStarter>>(){});
			return obj;
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				IOUtils.closeQuietly(reader);
			}
		}
	}
	
	public static String marshalJSON(Class<List<CandidateStarter>> iClass, Object payload) throws Exception {			
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}

}
