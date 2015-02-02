/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.deployment.entities;

import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

@JsonPropertyOrder({ "id", "url", "dataUrl", "mediaType", "type" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentResource {

	@JsonProperty("id")
	private String id;
	@JsonProperty("url")
	private String url;
	@JsonProperty("dataUrl")
	private String dataUrl;
	@JsonProperty("mediaType")
	private String mediaType;
	@JsonProperty("type")
	private String type;

	/**
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
	 * @return The dataUrl
	 */
	@JsonProperty("dataUrl")
	public String getDataUrl() {
		return dataUrl;
	}

	/**
	 * 
	 * @param dataUrl
	 *            The dataUrl
	 */
	@JsonProperty("dataUrl")
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	/**
	 * 
	 * @return The mediaType
	 */
	@JsonProperty("mediaType")
	public String getMediaType() {
		return mediaType;
	}

	/**
	 * 
	 * @param mediaType
	 *            The mediaType
	 */
	@JsonProperty("mediaType")
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public static List<DeploymentResource> unMarshalJSON(
			Class<List<DeploymentResource>> iClass, String payload)
			throws Exception {
		StringReader reader = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			reader = new StringReader(payload);
			List<DeploymentResource> obj = mapper.readValue(reader,
					new TypeReference<List<DeploymentResource>>() {
					});
			return obj;
		} catch (Exception e) {
			throw e;
		} finally {
			if (reader != null) {
				IOUtils.closeQuietly(reader);
			}
		}
	}

	public static String marshalJSON(Class<List<DeploymentResource>> iClass,
			Object payload) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}

}
