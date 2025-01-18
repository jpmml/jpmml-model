/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.ByteArrayOutputStream;

import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.Serializer;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ComplexValueTest {

	@Test
	public void marshal() throws Exception {
		checkConstant("", null);

		checkConstant("1", 1);
		checkConstant("1.0", 1.0d);

		Class<String> stringClazz = String.class;

		checkConstant("class java.lang.String", stringClazz);

		ComplexValue stringClazzWrapper = new ComplexValue(){

			@Override
			public Object toSimpleValue(){
				return stringClazz.getName();
			}
		};

		checkConstant("java.lang.String", stringClazzWrapper);
	}

	static
	private void checkConstant(String expectedValue, Object value) throws Exception {
		Constant constant = new Constant()
			.setValue(value);

		String string;

		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			Serializer serializer = new JAXBSerializer();

			serializer.serialize(constant, os);

			string = os.toString("UTF-8");
		}

		// XXX
		string = string.trim();

		if(("").equals(expectedValue)){
			assertTrue(string.contains("<Constant ") && string.endsWith("/>"));
		} else

		{
			assertTrue(string.contains(">" + expectedValue + "</Constant>"));
		}
	}
}