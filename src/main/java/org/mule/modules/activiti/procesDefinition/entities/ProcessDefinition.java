/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.procesDefinition.entities;

import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id", "url", "version", "key", "category", "suspended",
		"name", "description", "deploymentId", "deploymentUrl",
		"graphicalNotationDefined", "resource", "diagramResource",
		"startFormDefined", "tenantId" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessDefinition {

	@JsonProperty("id")
	private String id;
	@JsonProperty("url")
	private String url;
	@JsonProperty("version")
	private int version;
	@JsonProperty("key")
	private String key;
	@JsonProperty("category")
	private String category;
	@JsonProperty("suspended")
	private boolean suspended;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("deploymentId")
	private String deploymentId;
	@JsonProperty("deploymentUrl")
	private String deploymentUrl;
	@JsonProperty("graphicalNotationDefined")
	private boolean graphicalNotationDefined;
	@JsonProperty("resource")
	private String resource;
	@JsonProperty("diagramResource")
	private String diagramResource;
	@JsonProperty("startFormDefined")
	private boolean startFormDefined;
	@JsonProperty("tenantId")
	private String tenantId;

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

	public ProcessDefinition withId(String id) {
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

	public ProcessDefinition withUrl(String url) {
		this.url = url;
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

	public ProcessDefinition withVersion(int version) {
		this.version = version;
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

	public ProcessDefinition withKey(String key) {
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

	public ProcessDefinition withCategory(String category) {
		this.category = category;
		return this;
	}

	/**
	 * 
	 * @return The suspended
	 */
	@JsonProperty("suspended")
	public boolean isSuspended() {
		return suspended;
	}

	/**
	 * 
	 * @param suspended
	 *            The suspended
	 */
	@JsonProperty("suspended")
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public ProcessDefinition withSuspended(boolean suspended) {
		this.suspended = suspended;
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

	public ProcessDefinition withName(String name) {
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

	public ProcessDefinition withDescription(String description) {
		this.description = description;
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

	public ProcessDefinition withDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
		return this;
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

	public ProcessDefinition withDeploymentUrl(String deploymentUrl) {
		this.deploymentUrl = deploymentUrl;
		return this;
	}

	/**
	 * 
	 * @return The graphicalNotationDefined
	 */
	@JsonProperty("graphicalNotationDefined")
	public boolean isGraphicalNotationDefined() {
		return graphicalNotationDefined;
	}

	/**
	 * 
	 * @param graphicalNotationDefined
	 *            The graphicalNotationDefined
	 */
	@JsonProperty("graphicalNotationDefined")
	public void setGraphicalNotationDefined(boolean graphicalNotationDefined) {
		this.graphicalNotationDefined = graphicalNotationDefined;
	}

	public ProcessDefinition withGraphicalNotationDefined(
			boolean graphicalNotationDefined) {
		this.graphicalNotationDefined = graphicalNotationDefined;
		return this;
	}

	/**
	 * 
	 * @return The resource
	 */
	@JsonProperty("resource")
	public String getResource() {
		return resource;
	}

	/**
	 * 
	 * @param resource
	 *            The resource
	 */
	@JsonProperty("resource")
	public void setResource(String resource) {
		this.resource = resource;
	}

	public ProcessDefinition withResource(String resource) {
		this.resource = resource;
		return this;
	}

	/**
	 * 
	 * @return The diagramResource
	 */
	@JsonProperty("diagramResource")
	public String getDiagramResource() {
		return diagramResource;
	}

	/**
	 * 
	 * @param diagramResource
	 *            The diagramResource
	 */
	@JsonProperty("diagramResource")
	public void setDiagramResource(String diagramResource) {
		this.diagramResource = diagramResource;
	}

	public ProcessDefinition withDiagramResource(String diagramResource) {
		this.diagramResource = diagramResource;
		return this;
	}

	/**
	 * 
	 * @return The startFormDefined
	 */
	@JsonProperty("startFormDefined")
	public boolean isStartFormDefined() {
		return startFormDefined;
	}

	/**
	 * 
	 * @param startFormDefined
	 *            The startFormDefined
	 */
	@JsonProperty("startFormDefined")
	public void setStartFormDefined(boolean startFormDefined) {
		this.startFormDefined = startFormDefined;
	}

	public ProcessDefinition withStartFormDefined(boolean startFormDefined) {
		this.startFormDefined = startFormDefined;
		return this;
	}
	
	public static Object unMarshalJSON(Class<ProcessDefinition> iClass, String payload)
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
	
	public static String marshalJSON(Class<ProcessDefinition> iClass, Object payload) throws Exception {			
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}

}
