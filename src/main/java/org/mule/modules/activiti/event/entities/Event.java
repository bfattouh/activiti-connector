/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package org.mule.modules.activiti.event.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * 
 * @author bfattouh
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "action", "id", "message", "taskUrl", "time", "url",
		"userId" })
public class Event {

	@JsonProperty("action")
	private String action;
	@JsonProperty("id")
	private String id;
	@JsonProperty("message")
	private List<String> message = new ArrayList<String>();
	@JsonProperty("taskUrl")
	private String taskUrl;
	@JsonProperty("time")
	private String time;
	@JsonProperty("url")
	private String url;
	@JsonProperty("userId")
	private Object userId;

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

	public Event withAction(String action) {
		this.action = action;
		return this;
	}

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public Event withId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @return The message
	 */
	@JsonProperty("message")
	public List<String> getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 *            The message
	 */
	@JsonProperty("message")
	public void setMessage(List<String> message) {
		this.message = message;
	}

	public Event withMessage(List<String> message) {
		this.message = message;
		return this;
	}

	/**
	 * 
	 * @return The taskUrl
	 */
	@JsonProperty("taskUrl")
	public String getTaskUrl() {
		return taskUrl;
	}

	/**
	 * 
	 * @param taskUrl
	 *            The taskUrl
	 */
	@JsonProperty("taskUrl")
	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public Event withTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
		return this;
	}

	/**
	 * 
	 * @return The time
	 */
	@JsonProperty("time")
	public String getTime() {
		return time;
	}

	/**
	 * 
	 * @param time
	 *            The time
	 */
	@JsonProperty("time")
	public void setTime(String time) {
		this.time = time;
	}

	public Event withTime(String time) {
		this.time = time;
		return this;
	}

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

	public Event withUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 
	 * @return The userId
	 */
	@JsonProperty("userId")
	public Object getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId
	 *            The userId
	 */
	@JsonProperty("userId")
	public void setUserId(Object userId) {
		this.userId = userId;
	}

	public Event withUserId(Object userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(action).append(id).append(message)
				.append(taskUrl).append(time).append(url).append(userId)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Event) == false) {
			return false;
		}
		Event rhs = ((Event) other);
		return new EqualsBuilder().append(action, rhs.action)
				.append(id, rhs.id).append(message, rhs.message)
				.append(taskUrl, rhs.taskUrl).append(time, rhs.time)
				.append(url, rhs.url).append(userId, rhs.userId).isEquals();
	}

}
