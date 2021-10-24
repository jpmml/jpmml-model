/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.stream.StreamResult;

import jakarta.xml.bind.JAXBException;
import org.jpmml.model.JAXBUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ComplexValueTest {

	@Test
	public void marshal() throws Exception {
		checkConstant("", null);

		checkConstant("1", new Integer(1));
		checkConstant("1.0", new Double(1.0d));

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
	private void checkConstant(String expectedValue, Object value) throws IOException, JAXBException {
		Constant constant = new Constant()
			.setValue(value);

		String string;

		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			JAXBUtil.marshal(constant, new StreamResult(os));

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