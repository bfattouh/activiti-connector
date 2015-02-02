/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti.candidateStarter.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "candidateStarters" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidateStartersWrapper {

	@JsonProperty("candidateStarters")
	private List<CandidateStarter> candidateStarters = new ArrayList<CandidateStarter>();

	/**
	 * 
	 * @return The candidateStarters
	 */
	@JsonProperty("candidateStarters")
	public List<CandidateStarter> getCandidateStarters() {
		return candidateStarters;
	}

	/**
	 * 
	 * @param candidateStarters
	 *            The candidateStarters
	 */
	@JsonProperty("candidateStarters")
	public void setData(List<CandidateStarter> candidateStarters) {
		this.candidateStarters = candidateStarters;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
