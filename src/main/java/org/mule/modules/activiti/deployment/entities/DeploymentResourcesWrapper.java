/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.deployment.entities;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

@JsonPropertyOrder({ "deploymentResources" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentResourcesWrapper {

	@JsonProperty("deploymentResources")
	private List<DeploymentResource> deploymentResources = new ArrayList<DeploymentResource>();

	/**
	 * 
	 * @return The deploymentResources
	 */
	@JsonProperty("deploymentResources")
	public List<DeploymentResource> getDeploymentResources() {
		return deploymentResources;
	}

	/**
	 * 
	 * @param deploymentResources
	 *            The deploymentResources
	 */
	@JsonProperty("deploymentResources")
	public void setDeploymentResources(
			List<DeploymentResource> deploymentResources) {
		this.deploymentResources = deploymentResources;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public static Object unMarshalJSON(
			Class<DeploymentResourcesWrapper> iClass, String payload)
			throws Exception {
		StringReader reader = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			reader = new StringReader(payload);
			DeploymentResourcesWrapper obj = mapper.readValue(reader,
					new TypeReference<DeploymentResourcesWrapper>() {
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

	public static String marshalJSON(Class<DeploymentResourcesWrapper> iClass,
			Object payload) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}

}
