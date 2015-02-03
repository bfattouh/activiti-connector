/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.tasks.automation.testcases;

import java.io.Serializable;
/**
 * 
 * @author bfattouh
 *
 */
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String street;
	private String city;

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return this.street;
	}

	public String getCity() {
		return this.city;
	}

	@Override
	public String toString() {
		return new StringBuffer(" Street : ").append(this.street)
				.append(" -- City : ").append(this.city).toString();
	}

}
