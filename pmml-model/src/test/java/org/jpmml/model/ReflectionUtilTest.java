/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;

import org.dmg.pmml.CustomPMML;
import org.dmg.pmml.PMML;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}