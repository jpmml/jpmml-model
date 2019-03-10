/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.transform.stream.StreamResult;

import org.jpmml.model.JAXBUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ComplexArrayTest {

	@Test
	public void marshal() throws Exception {
		checkArray("1 2 3", Array.Type.INT, Arrays.asList(1, 2, 3));
		checkArray("1.0 2.0 3.0", Array.Type.REAL, Arrays.asList(1d, 2d, 3d));

		ComplexValue stringTwentyTwoWrapper = new ComplexValue(){

			@Override
			public Object toSimpleValue(){
				return "22";
			}
		};

		checkArray("1 22 3", Array.Type.STRING, Arrays.asList("1", stringTwentyTwoWrapper, "3"));
	}

	static
	public void checkArray(String expectedValue, Array.Type type, Collection<?> objects) throws Exception {
		ComplexArray array = new ComplexArray(type, objects);

		String string;

		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			JAXBUtil.marshal(array, new StreamResult(os));

			string = os.toString("UTF-8");
		}

		assertTrue(string.contains(">" + expectedValue + "</Array>"));
	}
}