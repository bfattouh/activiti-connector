/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.procesDefinition.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "category" })
public class ProcessDefinitionCategoryRequest {

	@JsonProperty("category")
	private String category;

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

	public ProcessDefinitionCategoryRequest withCategory(String category) {
		this.category = category;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
