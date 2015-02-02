/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.activiti;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.mule.modules.activiti.variable.entities.VariableRequest;
import org.mule.modules.activiti.variable.entities.VariableValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Bouchaib Fattouh Appnovation Technologies
 */
public class ConnectorHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(ConnectorHelper.class);

	protected static String setBasicAuthorization(String username,
			String password) {
		String authString = username + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		logger.debug("Basic authorization encoded credentials: {}",
				authStringEnc);
		return "Basic " + authStringEnc;
	}

	public static String toISO8601Date(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}

	protected static void validateCredentials(String username, String password) {
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException("The username may not be null");
		}
		if (StringUtils.isBlank(password)) {
			throw new IllegalArgumentException("The password may not be null");
		}
	}

	protected static NameValuePair getNameValuePair(String name, String value) {
		NameValuePair nameValuePair = null;
		if (value != null) {
			nameValuePair = new BasicNameValuePair(name, value);
		}
		return nameValuePair;
	}

	protected static List<NameValuePair> getParams(String aname,
			String nameLike, String category, String categoryNotEquals,
			String tenantId, Boolean tenantIdLike, Boolean withoutTenantId,
			String sort) {
		NameValuePair nameValuePair = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (aname != null) {
			nameValuePair = new BasicNameValuePair("name", aname);
			params.add(nameValuePair);
		}
		if (nameLike != null) {
			nameValuePair = new BasicNameValuePair("nameLike", nameLike);
			params.add(nameValuePair);
		}
		if (category != null) {
			nameValuePair = new BasicNameValuePair("category", category);
			params.add(nameValuePair);
		}
		if (categoryNotEquals != null) {
			nameValuePair = new BasicNameValuePair("categoryNotEquals",
					categoryNotEquals);
			params.add(nameValuePair);
		}
		if (tenantId != null) {
			nameValuePair = new BasicNameValuePair("tenantId", tenantId);
			params.add(nameValuePair);
		}
		if (tenantIdLike != null) {
			nameValuePair = new BasicNameValuePair("tenantIdLike",
					Boolean.toString(tenantIdLike));
			params.add(nameValuePair);
		}
		if (withoutTenantId != null) {
			nameValuePair = new BasicNameValuePair("withoutTenantId",
					Boolean.toString(withoutTenantId));
			params.add(nameValuePair);
		}
		if (sort != null) {
			nameValuePair = new BasicNameValuePair("sort", sort);
			params.add(nameValuePair);
		}
		return params;
	}

	public static List<VariableRequest> fromMapToVariableRequests(
			Map<String, VariableValueType> variables) {
		VariableRequest variableRequest = null;
		List<VariableRequest> variableRequests = new ArrayList<VariableRequest>();
		for (String name : variables.keySet()) {
			VariableValueType variableValueType = variables.get(name);
			variableRequest = new VariableRequest(name,
					variableValueType.getType(), variableValueType.getValue());
			variableRequests.add(variableRequest);
		}
		return variableRequests;
	}

}
