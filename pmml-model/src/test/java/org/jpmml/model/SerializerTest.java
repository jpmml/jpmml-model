/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.PMMLObject;

import static org.junit.jupiter.api.Assertions.assertTrue;

abstract
public class SerializerTest {

	static
	protected <E extends PMMLObject> E checkedClone(Serializer serializer, E object) throws Exception {
		E clonedObject = SerializationUtil.clone(serializer, object);

		assertTrue(ReflectionUtil.equals(object, clonedObject));

		return clonedObject;
	}
}