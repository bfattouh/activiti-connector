/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.attachment.entities;

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
@JsonPropertyOrder({ "id", "url", "name", "description", "type", "taskUrl",
		"processInstanceUrl", "externalUrl", "contentUrl" })
public class Attachment {

	@JsonProperty("id")
	private String id;
	@JsonProperty("url")
	private String url;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("type")
	private String type;
	@JsonProperty("taskUrl")
	private String taskUrl;
	@JsonProperty("processInstanceUrl")
	private Object processInstanceUrl;
	@JsonProperty("externalUrl")
	private String externalUrl;
	@JsonProperty("contentUrl")
	private Object contentUrl;

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

	public Attachment withId(String id) {
		this.id = id;
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

	public Attachment withUrl(String url) {
		this.url = url;
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

	public Attachment withName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 
	 * @return The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	public Attachment withDescription(String description) {
		this.description = description;
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

	public Attachment withType(String type) {
		this.type = type;
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

	public Attachment withTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
		return this;
	}

	/**
	 * 
	 * @return The processInstanceUrl
	 */
	@JsonProperty("processInstanceUrl")
	public Object getProcessInstanceUrl() {
		return processInstanceUrl;
	}

	/**
	 * 
	 * @param processInstanceUrl
	 *            The processInstanceUrl
	 */
	@JsonProperty("processInstanceUrl")
	public void setProcessInstanceUrl(Object processInstanceUrl) {
		this.processInstanceUrl = processInstanceUrl;
	}

	public Attachment withProcessInstanceUrl(Object processInstanceUrl) {
		this.processInstanceUrl = processInstanceUrl;
		return this;
	}

	/**
	 * 
	 * @return The externalUrl
	 */
	@JsonProperty("externalUrl")
	public String getExternalUrl() {
		return externalUrl;
	}

	/**
	 * 
	 * @param externalUrl
	 *            The externalUrl
	 */
	@JsonProperty("externalUrl")
	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public Attachment withExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
		return this;
	}

	/**
	 * 
	 * @return The contentUrl
	 */
	@JsonProperty("contentUrl")
	public Object getContentUrl() {
		return contentUrl;
	}

	/**
	 * 
	 * @param contentUrl
	 *            The contentUrl
	 */
	@JsonProperty("contentUrl")
	public void setContentUrl(Object contentUrl) {
		this.contentUrl = contentUrl;
	}

	public Attachment withContentUrl(Object contentUrl) {
		this.contentUrl = contentUrl;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(url).append(name)
				.append(description).append(type).append(taskUrl)
				.append(processInstanceUrl).append(externalUrl)
				.append(contentUrl).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Attachment) == false) {
			return false;
		}
		Attachment rhs = ((Attachment) other);
		return new EqualsBuilder().append(id, rhs.id).append(url, rhs.url)
				.append(name, rhs.name).append(description, rhs.description)
				.append(type, rhs.type).append(taskUrl, rhs.taskUrl)
				.append(processInstanceUrl, rhs.processInstanceUrl)
				.append(externalUrl, rhs.externalUrl)
				.append(contentUrl, rhs.contentUrl).isEquals();
	}

}
