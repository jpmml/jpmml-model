/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.CustomPMML;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.Header;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.Value;
import org.dmg.pmml.Version;
import org.dmg.pmml.regression.RegressionModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ReflectionUtilTest {

	@Test
	public void equals(){
		DataField left = new DataField()
			.setName("x")
			.setCyclic(null);

		DataField right = new DataField()
			.setName("x")
			.setCyclic(DataField.Cyclic.ZERO);

		// Initialize a live list instance
		right.getValues();

		assertTrue(ReflectionUtil.equals(left, right));

		Value leftValue = new Value()
			.setValue(0)
			.setProperty(null);

		Value rightValue = new Value()
			.setValue(0)
			.setProperty(Value.Property.VALID);

		right.addValues(rightValue);

		assertFalse(ReflectionUtil.equals(left, right));

		left.addValues(leftValue);

		assertTrue(ReflectionUtil.equals(left, right));

		// Double != Integer
		leftValue.setValue(((Number)rightValue.requireValue()).doubleValue());

		assertFalse(ReflectionUtil.equals(left, right));

		leftValue.setValue(rightValue.requireValue());

		assertTrue(ReflectionUtil.equals(left, right));

		Value missingValue = new Value()
			.setValue(-999)
			.setProperty(Value.Property.MISSING);

		right.addValues(missingValue);

		assertFalse(ReflectionUtil.equals(left, right));
	}

	@Test
	public void copyState(){
		PMML pmml = new PMML(Version.PMML_4_4.getVersion(), new Header(), new DataDictionary());

		// Initialize a live list instance
		pmml.getModels();

		CustomPMML customPmml = new CustomPMML();

		ReflectionUtil.copyState(pmml, customPmml);

		assertSame(pmml.requireVersion(), customPmml.requireVersion());
		assertSame(pmml.requireHeader(), customPmml.requireHeader());
		assertSame(pmml.requireDataDictionary(), customPmml.requireDataDictionary());

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
		ReflectionUtil.getField(PMML.class, "version");
		ReflectionUtil.getField(CustomPMML.class, "version");
	}

	@Test
	public void getFields(){
		List<Field> fields = ReflectionUtil.getFields(PMML.class);
		List<Field> customFields = ReflectionUtil.getFields(CustomPMML.class);

		assertEquals(1 /* PMMLObject */ + 8 /* PMML */, fields.size());

		assertEquals(new HashSet<>(fields), new HashSet<>(customFields));
	}

	@Test
	public void getGetterMethods(){
		Map<Field, Method> getterMethods = ReflectionUtil.getGetterMethods(OutputField.class);

		assertEquals(1 /* PMMLObject */ + 20 /* OutputField */, getterMethods.size());

		try {
			Field field = OutputField.class.getDeclaredField("DEFAULT_RANK");

			ReflectionUtil.getGetterMethod(field);

			fail();
		} catch(ReflectiveOperationException roe){
			fail();
		} catch(RuntimeException re){
			// Ignored
		}
	}

	@Test
	public void getAttributeGetterMethods(){
		List<Field> fields = ReflectionUtil.getAttributeFields();

		for(Field field : fields){
			Method getterMethod = ReflectionUtil.getGetterMethod(field);

			assertNotNull(getterMethod);
		}
	}

	@Test
	public void getElementGetterMethods(){
		List<Field> fields = ReflectionUtil.getElementFields();

		for(Field field : fields){
			Method getterMethod = ReflectionUtil.getGetterMethod(field);

			assertNotNull(getterMethod);
		}
	}

	@Test
	public void getValue(){
		PMML pmml = new CustomPMML();
		pmml.setVersion(Version.PMML_4_4.getVersion());

		assertEquals("4.4", pmml.requireVersion());
		assertEquals("4.4", pmml.getBaseVersion());

		Field versionField = PMMLAttributes.PMML_VERSION;
		Field baseVersionField = PMMLAttributes.PMML_BASEVERSION;

		assertEquals("4.4", ReflectionUtil.getFieldValue(versionField, pmml));
		assertEquals((String)null, ReflectionUtil.getFieldValue(baseVersionField, pmml));

		Method versionGetterMethod = ReflectionUtil.getGetterMethod(versionField);
		Method baseVersionGetterMethod = ReflectionUtil.getGetterMethod(baseVersionField);

		assertEquals("4.4", ReflectionUtil.getGetterMethodValue(versionGetterMethod, pmml));
		assertEquals("4.4", ReflectionUtil.getGetterMethodValue(baseVersionGetterMethod, pmml));
	}

	@Test
	public void isPrimitiveWrapper(){
		assertFalse(ReflectionUtil.isPrimitiveWrapper(String.class));

		assertTrue(ReflectionUtil.isPrimitiveWrapper(Integer.class));
		assertTrue(ReflectionUtil.isPrimitiveWrapper(Double.class));
		assertTrue(ReflectionUtil.isPrimitiveWrapper(Boolean.class));
	}
}
