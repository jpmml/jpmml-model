/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.TextSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComplexArrayTest {

	@Test
	public void marshalList() throws Exception {
		ComplexArray array = new ComplexArray()
			.setType(Array.Type.INT)
			.setValue(Arrays.asList(1, 2, 3));

		checkArray("1 2 3", array);

		array
			.setType(Array.Type.REAL)
			.setValue(Arrays.asList(1d, 2d, 3d));

		checkArray("1.0 2.0 3.0", array);

		Collection<Object> value = (Collection)array.getValue();

		value.add(1d);

		checkArray("1.0 2.0 3.0 1.0", array);
	}

	@Test
	public void marshalSet() throws Exception {
		ComplexArray array = new ComplexArray()
			.setType(Array.Type.STRING)
			.setValue(Collections.emptySet());

		ComplexValue stringTwentyTwoWrapper = new ComplexValue(){

			@Override
			public Object toSimpleValue(){
				return "22";
			}
		};

		Collection<Object> value = (Collection)array.getValue();

		value.addAll(Arrays.asList("1", stringTwentyTwoWrapper, "3"));

		checkArray("1 22 3", array);

		value.add("1");

		checkArray("1 22 3", array);

		value.add("\"four\"");

		checkArray("1 22 3 \\&quot;four\\&quot;", array);
	}

	static
	public void checkArray(String expectedValue, Array array) throws Exception {
		TextSerializer serializer = new JAXBSerializer();

		String string = SerializationUtil.toString(serializer, array);

		assertTrue(string.contains(">" + expectedValue + "</Array>"));
	}
}