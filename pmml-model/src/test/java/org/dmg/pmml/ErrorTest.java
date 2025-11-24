/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.Serializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorTest {

	@Test
	public void jaxbClone() throws Exception {
		Serializer serializer = new JAXBSerializer();

		Error error = new Error()
			.setMessage("This should never happen");

		Error jaxbError = SerializationUtil.clone(serializer, error);

		assertEquals(error.getMessage(), jaxbError.getMessage());
	}
}