/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.model.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "name", "key", "category", "version", "metaInfo",
		"deploymentId", "tenantId" })
public class ModelRequest {

	@JsonProperty("name")
	private String name;
	@JsonProperty("key")
	private String key;
	@JsonProperty("category")
	private String category;
	@JsonProperty("version")
	private int version;
	@JsonProperty("metaInfo")
	private String metaInfo;
	@JsonProperty("deploymentId")
	private String deploymentId;
	@JsonProperty("tenantId")
	private String tenantId;

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

	public ModelRequest withName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 
	 * @return The key
	 */
	@JsonProperty("key")
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @param key
	 *            The key
	 */
	@JsonProperty("key")
	public void setKey(String key) {
		this.key = key;
	}

	public ModelRequest withKey(String key) {
		this.key = key;
		return this;
	}

	/**
	 * 
	 * @return The category
	 */
	@JsonProperty("category")
	public String getCategory() {
		return category;
	}

	/**
	 * 
	 * @param category
	 *            The category
	 */
	@JsonProperty("category")
	public void setCategory(String category) {
		this.category = category;
	}

	public ModelRequest withCategory(String category) {
		this.category = category;
		return this;
	}

	/**
	 * 
	 * @return The version
	 */
	@JsonProperty("version")
	public int getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 *            The version
	 */
	@JsonProperty("version")
	public void setVersion(int version) {
		this.version = version;
	}

	public ModelRequest withVersion(int version) {
		this.version = version;
		return this;
	}

	/**
	 * 
	 * @return The metaInfo
	 */
	@JsonProperty("metaInfo")
	public String getMetaInfo() {
		return metaInfo;
	}

	/**
	 * 
	 * @param metaInfo
	 *            The metaInfo
	 */
	@JsonProperty("metaInfo")
	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}

	public ModelRequest withMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
		return this;
	}

	/**
	 * 
	 * @return The deploymentId
	 */
	@JsonProperty("deploymentId")
	public String getDeploymentId() {
		return deploymentId;
	}

	/**
	 * 
	 * @param deploymentId
	 *            The deploymentId
	 */
	@JsonProperty("deploymentId")
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public ModelRequest withDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
		return this;
	}

	/**
	 * 
	 * @return The tenantId
	 */
	@JsonProperty("tenantId")
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * 
	 * @param tenantId
	 *            The tenantId
	 */
	@JsonProperty("tenantId")
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public ModelRequest withTenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
