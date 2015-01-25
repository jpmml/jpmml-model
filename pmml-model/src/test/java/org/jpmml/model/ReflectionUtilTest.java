/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;

import org.dmg.pmml.CustomPMML;
import org.dmg.pmml.DataType;
import org.dmg.pmml.PMML;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReflectionUtilTest {

	@Test
	public void getField(){
		ReflectionUtil.getField(new PMML(), "version");
		ReflectionUtil.getField(new CustomPMML(), "version");
	}

	@Test
	public void getAllFields(){
		List<Field> fields = ReflectionUtil.getAllFields(new PMML());
		List<Field> customFields = ReflectionUtil.getAllFields(new CustomPMML());

		assertEquals(new HashSet<Field>(fields), new HashSet<Field>(customFields));
	}

	@Test
	public void isPrimitiveWrapper(){
		assertTrue(ReflectionUtil.isPrimitiveWrapper(new Integer(0)));
		assertTrue(ReflectionUtil.isPrimitiveWrapper(new Double(0d)));
		assertTrue(ReflectionUtil.isPrimitiveWrapper(new Boolean(false)));

		assertFalse(ReflectionUtil.isPrimitiveWrapper(new String("")));
	}

	@Test
	public void isEnum(){
		assertTrue(ReflectionUtil.isEnum(DataType.STRING));
	}
}