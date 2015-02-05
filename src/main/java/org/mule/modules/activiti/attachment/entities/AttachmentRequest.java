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
@JsonPropertyOrder({ "name", "description", "type", "externalUrl" })
public class AttachmentRequest {

	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("type")
	private String type;
	@JsonProperty("externalUrl")
	private String externalUrl;

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

	public AttachmentRequest withName(String name) {
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

	public AttachmentRequest withDescription(String description) {
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

	public AttachmentRequest withType(String type) {
		this.type = type;
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

	public AttachmentRequest withExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(description)
				.append(type).append(externalUrl).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof AttachmentRequest) == false) {
			return false;
		}
		AttachmentRequest rhs = ((AttachmentRequest) other);
		return new EqualsBuilder().append(name, rhs.name)
				.append(description, rhs.description).append(type, rhs.type)
				.append(externalUrl, rhs.externalUrl).isEquals();
	}

}
