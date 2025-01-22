/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;

import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.PMMLElements;
import org.dmg.pmml.ResultFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionInspectorTest {

	@Test
	public void isAttribute(){
		assertTrue(VersionInspector.isAttribute(PMMLAttributes.OUTPUTFIELD_NAME));

		assertFalse(VersionInspector.isAttribute(PMMLElements.OUTPUTFIELD_EXTENSIONS));
	}

	@Test
	public void isEnumValue() throws NoSuchFieldException {
		Field field = ResultFeature.class.getField("PREDICTED_VALUE");

		assertTrue(VersionInspector.isEnumValue(field));

		assertFalse(VersionInspector.isEnumValue(PMMLAttributes.OUTPUTFIELD_RESULTFEATURE));
	}

	@Test
	public void isElement(){
		assertFalse(VersionInspector.isElement(PMMLAttributes.OUTPUTFIELD_NAME));

		assertTrue(VersionInspector.isElement(PMMLElements.OUTPUTFIELD_EXTENSIONS));
		assertTrue(VersionInspector.isElement(PMMLElements.OUTPUTFIELD_EXPRESSION));
	}
}