/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.procesDefinition.entities;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;



@JsonPropertyOrder({ "data", "total", "start", "sort", "order", "size" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessDefinitionsWrapper {

	@JsonProperty("data")
	private List<ProcessDefinition> data = new ArrayList<ProcessDefinition>();
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
	public List<ProcessDefinition> getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 *            The data
	 */
	@JsonProperty("data")
	public void setData(List<ProcessDefinition> data) {
		this.data = data;
	}

	public ProcessDefinitionsWrapper withData(List<ProcessDefinition> data) {
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

	public ProcessDefinitionsWrapper withTotal(int total) {
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

	public ProcessDefinitionsWrapper withStart(int start) {
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

	public ProcessDefinitionsWrapper withSort(String sort) {
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

	public ProcessDefinitionsWrapper withOrder(String order) {
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

	public ProcessDefinitionsWrapper withSize(int size) {
		this.size = size;
		return this;
	}
	
	public static Object unMarshalJSON(Class<ProcessDefinitionsWrapper> iClass, String payload)
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
	
	public static String marshalJSON(Class<ProcessDefinitionsWrapper> iClass, Object payload) throws Exception {			
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(payload);
	}
	
	public ProcessDefinition getProcessDefinitionByName(String name){
		for (ProcessDefinition processDefinition : data) {
			if(processDefinition.getName().equals(name)){
				return processDefinition;
			}
		}
		return null;
	}


}
