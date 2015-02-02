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
@JsonPropertyOrder({ "id", "url", "name", "key", "category", "version",
		"metaInfo", "deploymentId", "deploymentUrl", "createTime",
		"lastUpdateTime", "tenantId" })
public class Model {

	@JsonProperty("id")
	private String id;
	@JsonProperty("url")
	private String url;
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
	@JsonProperty("deploymentUrl")
	private String deploymentUrl;
	@JsonProperty("createTime")
	private String createTime;
	@JsonProperty("lastUpdateTime")
	private String lastUpdateTime;
	@JsonProperty("tenantId")
	private Object tenantId;

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

	/**
	 * 
	 * @return The deploymentUrl
	 */
	@JsonProperty("deploymentUrl")
	public String getDeploymentUrl() {
		return deploymentUrl;
	}

	/**
	 * 
	 * @param deploymentUrl
	 *            The deploymentUrl
	 */
	@JsonProperty("deploymentUrl")
	public void setDeploymentUrl(String deploymentUrl) {
		this.deploymentUrl = deploymentUrl;
	}

	/**
	 * 
	 * @return The createTime
	 */
	@JsonProperty("createTime")
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * 
	 * @param createTime
	 *            The createTime
	 */
	@JsonProperty("createTime")
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 
	 * @return The lastUpdateTime
	 */
	@JsonProperty("lastUpdateTime")
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * 
	 * @param lastUpdateTime
	 *            The lastUpdateTime
	 */
	@JsonProperty("lastUpdateTime")
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * 
	 * @return The tenantId
	 */
	@JsonProperty("tenantId")
	public Object getTenantId() {
		return tenantId;
	}

	/**
	 * 
	 * @param tenantId
	 *            The tenantId
	 */
	@JsonProperty("tenantId")
	public void setTenantId(Object tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
