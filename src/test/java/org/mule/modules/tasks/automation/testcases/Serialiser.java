/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.tasks.automation.testcases;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author bfattouh
 *
 */
public class Serialiser {

	public static void main(String args[]) {
		Serialiser serializer = new Serialiser();
		serializer.serializeAddress("Boulevard de Paris", "Montreal");
	}

	public void serializeAddress(String street, String city) {

		Address address = new Address();
		address.setStreet(street);
		address.setCity(city);

		try {
			FileOutputStream fout = new FileOutputStream("src/test/resources/address-2.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(address);
			oos.close();
			System.out.println("Serialization complete");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
