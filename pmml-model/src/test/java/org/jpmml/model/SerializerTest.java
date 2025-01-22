/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;

import org.dmg.pmml.PMMLObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract
public class SerializerTest {

	static
	protected <E extends PMMLObject> E checkedClone(Serializer serializer, E object) throws Exception {
		E clonedObject = clone(serializer, object);

		assertTrue(ReflectionUtil.equals(object, clonedObject));

		return clonedObject;
	}

	static
	protected <E extends PMMLObject> E clone(Serializer serializer, E object) throws Exception {
		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(10 * 1024);

		serializer.serialize(object, buffer);

		try(InputStream is = buffer.getInputStream()){
			@SuppressWarnings("unchecked")
			E clonedObject = (E)serializer.deserialize(is);

			assertEquals(-1, is.read());

			return clonedObject;
		}
	}
}