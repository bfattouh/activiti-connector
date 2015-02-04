/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.task.entities;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
"id",
"taskUrl",
"processInstanceUrl",
"message",
"author",
"time",
"taskId",
"processInstanceId"
})
public class Comment {

	@JsonProperty("id")
	private String id;
	@JsonProperty("taskUrl")
	private String taskUrl;
	@JsonProperty("processInstanceUrl")
	private String processInstanceUrl;
	@JsonProperty("message")
	private String message;
	@JsonProperty("author")
	private String author;
	@JsonProperty("time")
	private String time;
	@JsonProperty("taskId")
	private String taskId;
	@JsonProperty("processInstanceId")
	private String processInstanceId;
	
	/**
	* 
	* @return
	* The id
	*/
	@JsonProperty("id")
	public String getId() {
	return id;
	}

	/**
	* 
	* @param id
	* The id
	*/
	@JsonProperty("id")
	public void setId(String id) {
	this.id = id;
	}

	public Comment withId(String id) {
	this.id = id;
	return this;
	}

	/**
	* 
	* @return
	* The taskUrl
	*/
	@JsonProperty("taskUrl")
	public String getTaskUrl() {
	return taskUrl;
	}

	/**
	* 
	* @param taskUrl
	* The taskUrl
	*/
	@JsonProperty("taskUrl")
	public void setTaskUrl(String taskUrl) {
	this.taskUrl = taskUrl;
	}

	public Comment withTaskUrl(String taskUrl) {
	this.taskUrl = taskUrl;
	return this;
	}

	/**
	* 
	* @return
	* The processInstanceUrl
	*/
	@JsonProperty("processInstanceUrl")
	public String getProcessInstanceUrl() {
	return processInstanceUrl;
	}

	/**
	* 
	* @param processInstanceUrl
	* The processInstanceUrl
	*/
	@JsonProperty("processInstanceUrl")
	public void setProcessInstanceUrl(String processInstanceUrl) {
	this.processInstanceUrl = processInstanceUrl;
	}

	public Comment withProcessInstanceUrl(String processInstanceUrl) {
	this.processInstanceUrl = processInstanceUrl;
	return this;
	}

	/**
	* 
	* @return
	* The message
	*/
	@JsonProperty("message")
	public String getMessage() {
	return message;
	}

	/**
	* 
	* @param message
	* The message
	*/
	@JsonProperty("message")
	public void setMessage(String message) {
	this.message = message;
	}

	public Comment withMessage(String message) {
	this.message = message;
	return this;
	}

	/**
	* 
	* @return
	* The author
	*/
	@JsonProperty("author")
	public String getAuthor() {
	return author;
	}

	/**
	* 
	* @param author
	* The author
	*/
	@JsonProperty("author")
	public void setAuthor(String author) {
	this.author = author;
	}

	public Comment withAuthor(String author) {
	this.author = author;
	return this;
	}

	/**
	* 
	* @return
	* The time
	*/
	@JsonProperty("time")
	public String getTime() {
	return time;
	}

	/**
	* 
	* @param time
	* The time
	*/
	@JsonProperty("time")
	public void setTime(String time) {
	this.time = time;
	}

	public Comment withTime(String time) {
	this.time = time;
	return this;
	}

	/**
	* 
	* @return
	* The taskId
	*/
	@JsonProperty("taskId")
	public String getTaskId() {
	return taskId;
	}

	/**
	* 
	* @param taskId
	* The taskId
	*/
	@JsonProperty("taskId")
	public void setTaskId(String taskId) {
	this.taskId = taskId;
	}

	public Comment withTaskId(String taskId) {
	this.taskId = taskId;
	return this;
	}

	/**
	* 
	* @return
	* The processInstanceId
	*/
	@JsonProperty("processInstanceId")
	public String getProcessInstanceId() {
	return processInstanceId;
	}

	/**
	* 
	* @param processInstanceId
	* The processInstanceId
	*/
	@JsonProperty("processInstanceId")
	public void setProcessInstanceId(String processInstanceId) {
	this.processInstanceId = processInstanceId;
	}

	public Comment withProcessInstanceId(String processInstanceId) {
	this.processInstanceId = processInstanceId;
	return this;
	}

	@Override
	public String toString() {
	return ToStringBuilder.reflectionToString(this);
	}


	@Override
	public int hashCode() {
	return new HashCodeBuilder().append(id).append(taskUrl).append(processInstanceUrl).append(message).append(author).append(time).append(taskId).append(processInstanceId).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
	if (other == this) {
	return true;
	}
	if ((other instanceof Comment) == false) {
	return false;
	}
	Comment rhs = ((Comment) other);
	return new EqualsBuilder().append(id, rhs.id).append(taskUrl, rhs.taskUrl).append(processInstanceUrl, rhs.processInstanceUrl).append(message, rhs.message).append(author, rhs.author).append(time, rhs.time).append(taskId, rhs.taskId).append(processInstanceId, rhs.processInstanceId).isEquals();
	}

}
