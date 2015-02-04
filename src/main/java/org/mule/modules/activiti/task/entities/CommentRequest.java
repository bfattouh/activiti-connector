/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.task.entities;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 
 * @author bfattouh
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "message", "saveProcessInstanceId" })
public class CommentRequest {

	@JsonProperty("message")
	private String message;
	@JsonProperty("saveProcessInstanceId")
	private Boolean saveProcessInstanceId;

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

	public CommentRequest withMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * 
	 * @return The saveProcessInstanceId
	 */
	@JsonProperty("saveProcessInstanceId")
	public Boolean getSaveProcessInstanceId() {
		return saveProcessInstanceId;
	}

	/**
	 * 
	 * @param saveProcessInstanceId
	 *            The saveProcessInstanceId
	 */
	@JsonProperty("saveProcessInstanceId")
	public void setSaveProcessInstanceId(Boolean saveProcessInstanceId) {
		this.saveProcessInstanceId = saveProcessInstanceId;
	}

	public CommentRequest withSaveProcessInstanceId(
			Boolean saveProcessInstanceId) {
		this.saveProcessInstanceId = saveProcessInstanceId;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(message)
				.append(saveProcessInstanceId).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof CommentRequest) == false) {
			return false;
		}
		CommentRequest rhs = ((CommentRequest) other);
		return new EqualsBuilder().append(message, rhs.message)
				.append(saveProcessInstanceId, rhs.saveProcessInstanceId)
				.isEquals();
	}

}
