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


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "data", "total", "start", "sort", "order", "size" })
public class DeploymentWrapper {

	@JsonProperty("data")
	private List<Deployment> data = new ArrayList<Deployment>();
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
	public List<Deployment> getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 *            The data
	 */
	@JsonProperty("data")
	public void setData(List<Deployment> data) {
		this.data = data;
	}

	public DeploymentWrapper withData(List<Deployment> data) {
		this.data = data;
		return this;
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

	public DeploymentWrapper withTotal(int total) {
		this.total = total;
		return this;
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

	public DeploymentWrapper withStart(int start) {
		this.start = start;
		return this;
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

	public DeploymentWrapper withSort(String sort) {
		this.sort = sort;
		return this;
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

	public DeploymentWrapper withOrder(String order) {
		this.order = order;
		return this;
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

	public DeploymentWrapper withSize(int size) {
		this.size = size;
		return this;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public static Object unMarshalJSON(Class<DeploymentWrapper> iClass, String payload)
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
	
	public static String marshalJSON(Class<DeploymentWrapper> iClass, Object payload) throws Exception {			
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}
	
	public Deployment getDeploymentByName(String name){
		for (Deployment deployment : data) {
			if(deployment.getName().equals(name)){
				return deployment;
			}
		}
		return null;
	}

}
