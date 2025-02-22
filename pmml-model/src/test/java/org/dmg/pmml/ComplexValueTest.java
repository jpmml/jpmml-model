/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.TextSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
		TextSerializer serializer = new JAXBSerializer();

		Constant constant = new Constant()
			.setValue(value);

		String string = SerializationUtil.toString(serializer, constant);

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