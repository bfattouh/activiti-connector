/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.deployment.entities;

import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;


@JsonPropertyOrder({ "id", "name", "deploymentTime", "category", "url", "tenantId" })		
@JsonIgnoreProperties(ignoreUnknown = true)
public class Deployment {

	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("deploymentTime")
	private String deploymentTime;
	@JsonProperty("category")
	private String category;
	@JsonProperty("url")
	private String url;
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

	public Deployment withId(String id) {
		this.id = id;
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

	public Deployment withName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 
	 * @return The deploymentTime
	 */
	@JsonProperty("deploymentTime")
	public String getDeploymentTime() {
		return deploymentTime;
	}

	/**
	 * 
	 * @param deploymentTime
	 *            The deploymentTime
	 */
	@JsonProperty("deploymentTime")
	public void setDeploymentTime(String deploymentTime) {
		this.deploymentTime = deploymentTime;
	}

	public Deployment withDeploymentTime(String deploymentTime) {
		this.deploymentTime = deploymentTime;
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

	public Deployment withCategory(String category) {
		this.category = category;
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

	public Deployment withUrl(String url) {
		this.url = url;
		return this;
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

	public Deployment withTenantId(Object tenantId) {
		this.tenantId = tenantId;
		return this;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public static Object unMarshalJSON(Class<Deployment> iClass, String payload)
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
	
	public static String marshalJSON(Class<Deployment> iClass, Object payload) throws Exception {			
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}


}
