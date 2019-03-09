/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamResult;

import org.jpmml.model.JAXBUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PrimitiveValueWrapperTest {

	@Test
	public void marshal() throws Exception {
		assertEquals("", toString(null));

		assertEquals("1", toString(new Integer(1)));
		assertEquals("1.0", toString(new Double(1.0d)));

		Class<String> stringClazz = String.class;

		assertEquals("class java.lang.String", toString(stringClazz));

		PrimitiveValueWrapper stringClazzWrapper = new PrimitiveValueWrapper(){

			@Override
			public Object unwrap(){
				return stringClazz.getName();
			}
		};

		assertEquals("java.lang.String", toString(stringClazzWrapper));
	}

	static
	private String toString(Object value) throws IOException, JAXBException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		Constant constant = new Constant()
			.setValue(value);

		JAXBUtil.marshal(constant, new StreamResult(os));

		String string = os.toString("UTF-8");

		// XXX
		string = string.trim();

		if(string.endsWith("</Constant>")){
			int end = string.lastIndexOf("</Constant>");
			int begin = string.lastIndexOf('>', end) + 1;

			return string.substring(begin, end);
		} else

		if(string.contains("<Constant ") && string.endsWith("/>")){
			return "";
		} else

		{
			throw new IllegalArgumentException();
		}
	}
}