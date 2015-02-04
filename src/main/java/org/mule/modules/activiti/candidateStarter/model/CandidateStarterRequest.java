/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.candidateStarter.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "user", "groupId", "type" })
public class CandidateStarterRequest {

	@JsonProperty("user")
	private String user;
	@JsonProperty("groupId")
	private String groupId;
	@JsonProperty("type")
	private String type;

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

	public CandidateStarterRequest withUser(String user) {
		this.user = user;
		return this;
	}
	

	/**
	 * 
	 * @return The groupId
	 */
	@JsonProperty("groupId")
	public String getGroupId() {
		return groupId;
	}

	/**
	 * 
	 * @param groupId
	 *            The groupId
	 */
	@JsonProperty("groupId")
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public CandidateStarterRequest withGroupId(String groupId) {
		this.groupId = groupId;
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

	public CandidateStarterRequest withType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
