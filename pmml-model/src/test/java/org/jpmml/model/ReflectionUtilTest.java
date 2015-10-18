/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;

import org.dmg.pmml.CustomPMML;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataType;
import org.dmg.pmml.Header;
import org.dmg.pmml.PMML;
import org.dmg.pmml.RegressionModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ReflectionUtilTest {

	@Test
	public void copyState(){
		PMML pmml = new PMML("4.2", new Header(), new DataDictionary());

		// Initialize the live list instance
		pmml.getModels();

		CustomPMML customPmml = new CustomPMML();

		ReflectionUtil.copyState(pmml, customPmml);

		assertSame(pmml.getVersion(), customPmml.getVersion());
		assertSame(pmml.getHeader(), customPmml.getHeader());
		assertSame(pmml.getDataDictionary(), customPmml.getDataDictionary());

		assertFalse(pmml.hasModels());
		assertFalse(customPmml.hasModels());

		pmml.addModels(new RegressionModel());

		assertTrue(pmml.hasModels());
		assertTrue(customPmml.hasModels());

		assertSame(pmml.getModels(), customPmml.getModels());

		try {
			ReflectionUtil.copyState(customPmml, pmml);

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}
	}

	@Test
	public void getField(){
		ReflectionUtil.getField(new PMML(), "version");
		ReflectionUtil.getField(new CustomPMML(), "version");
	}

	@Test
	public void getAllFields(){
		List<Field> fields = ReflectionUtil.getAllFields(new PMML());
		List<Field> customFields = ReflectionUtil.getAllFields(new CustomPMML());

		assertEquals(new HashSet<>(fields), new HashSet<>(customFields));
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