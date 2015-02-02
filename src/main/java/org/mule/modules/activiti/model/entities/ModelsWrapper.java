/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.model.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "data", "total", "start", "sort", "order", "size" })
public class ModelsWrapper {

	@JsonProperty("data")
	private List<Model> models = new ArrayList<Model>();
	@JsonProperty("total")
	private int total;
	@JsonProperty("start")
	private int start;
	@JsonProperty("sort")
	private String sort;
	@JsonProperty("order")
	private String order;
	@JsonProperty("size")
	private int size;

	/**
	 * 
	 * @return The data
	 */
	@JsonProperty("data")
	public List<Model> getModels() {
		return models;
	}

	/**
	 * 
	 * @param data
	 *            The data
	 */
	@JsonProperty("data")
	public void setModels(List<Model> models) {
		this.models = models;
	}

	/**
	 * 
	 * @return The total
	 */
	@JsonProperty("total")
	public int getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 *            The total
	 */
	@JsonProperty("total")
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * 
	 * @return The start
	 */
	@JsonProperty("start")
	public int getStart() {
		return start;
	}

	/**
	 * 
	 * @param start
	 *            The start
	 */
	@JsonProperty("start")
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 
	 * @return The sort
	 */
	@JsonProperty("sort")
	public String getSort() {
		return sort;
	}

	/**
	 * 
	 * @param sort
	 *            The sort
	 */
	@JsonProperty("sort")
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * 
	 * @return The order
	 */
	@JsonProperty("order")
	public String getOrder() {
		return order;
	}

	/**
	 * 
	 * @param order
	 *            The order
	 */
	@JsonProperty("order")
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * 
	 * @return The size
	 */
	@JsonProperty("size")
	public int getSize() {
		return size;
	}

	/**
	 * 
	 * @param size
	 *            The size
	 */
	@JsonProperty("size")
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Model getModelByDeploymentId(String deploymentId) {
		for (Model model : models) {
			if (model.getDeploymentId() != null
					&& model.getDeploymentId().equals(deploymentId)) {
				return model;
			}
		}
		return null;
	}

	public Model getModelByName(String name) {
		for (Model model : models) {
			if (model.getName().equals(name)) {
				return model;
			}
		}
		return null;
	}

}
