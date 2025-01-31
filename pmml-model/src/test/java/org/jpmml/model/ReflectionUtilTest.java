/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dmg.pmml.CustomPMML;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.Header;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.PMMLElements;
import org.dmg.pmml.Value;
import org.dmg.pmml.Version;
import org.dmg.pmml.regression.RegressionModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReflectionUtilTest {

	@Test
	public void identity(){
		DataField left = new DataField()
			.setName("x")
			.setCyclic(null);

		DataField right = new DataField()
			.setName("x")
			.setCyclic(DataField.Cyclic.ZERO);

		int leftHash = ReflectionUtil.hashCode(left);
		int rightHash = ReflectionUtil.hashCode(right);

		assertEquals(leftHash, rightHash);

		// Initialize a live list instance
		List<Value> rightValues = right.getValues();

		rightHash = ReflectionUtil.hashCode(right);

		assertEquals(leftHash, rightHash);
		assertTrue(ReflectionUtil.equals(left, right));

		Value leftValue = new Value()
			.setValue(0)
			.setProperty(null);

		Value rightValue = new Value()
			.setValue(0)
			.setProperty(Value.Property.VALID);

		assertEquals(ReflectionUtil.hashCode(leftValue), ReflectionUtil.hashCode(rightValue));
		assertTrue(ReflectionUtil.equals(leftValue, rightValue));

		assertEquals(0, rightValues.size());

		right.addValues(rightValue);

		assertEquals(1, rightValues.size());

		rightHash = ReflectionUtil.hashCode(right);

		// XXX
		//assertNotEquals(leftHash, rightHash);
		assertFalse(ReflectionUtil.equals(left, right));

		left.addValues(leftValue);

		leftHash = ReflectionUtil.hashCode(left);

		assertEquals(leftHash, rightHash);
		assertTrue(ReflectionUtil.equals(left, right));

		// Double != Integer
		leftValue.setValue(((Number)rightValue.requireValue()).doubleValue());

		leftHash = ReflectionUtil.hashCode(left);

		assertFalse(ReflectionUtil.equals(left, right));

		leftValue.setValue(rightValue.requireValue());

		leftHash = ReflectionUtil.hashCode(left);

		assertTrue(ReflectionUtil.equals(left, right));

		Value missingValue = new Value()
			.setValue(-999)
			.setProperty(Value.Property.MISSING);

		right.addValues(missingValue);

		assertEquals(2, rightValues.size());

		rightHash = ReflectionUtil.hashCode(right);

		// XXX
		//assertNotEquals(leftHash, rightHash);
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

		assertThrows(IllegalArgumentException.class, () -> ReflectionUtil.copyState(customPmml, pmml));
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

		assertThrows(RuntimeException.class, () -> {
			Field field = OutputField.class.getDeclaredField("DEFAULT_RANK");

			ReflectionUtil.getGetterMethod(field);
		});
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
	public void getSetterMethods(){
		Map<Field, Method> setterMethods = ReflectionUtil.getSetterMethods(OutputField.class);

		assertEquals(1 /* PMMLObject */ + 18 /* OutputField */, setterMethods.size());
	}

	@Test
	public void getAttributeSetterMethods(){
		List<Field> fields = ReflectionUtil.getAttributeFields();

		for(Field field : fields){
			Method setterMethod = ReflectionUtil.getSetterMethod(field);

			assertNotNull(setterMethod);
		}
	}

	@Test
	public void getElementSetterMethods(){
		List<Field> fields = ReflectionUtil.getElementFields();

		for(Field field : fields){
			Class<?> fieldType = field.getType();

			if(Objects.equals(List.class, fieldType)){
				assertThrows(RuntimeException.class, () -> ReflectionUtil.getSetterMethod(field));
			} else

			{
				Method setterMethod = ReflectionUtil.getSetterMethod(field);

				assertNotNull(setterMethod);
			}
		}
	}

	@Test
	public void getAppenderMethods(){
		assertThrows(RuntimeException.class, () -> ReflectionUtil.getAppenderMethod(PMMLElements.OUTPUTFIELD_EXPRESSION));

		assertNotNull(ReflectionUtil.getAppenderMethod(PMMLElements.OUTPUTFIELD_EXTENSIONS));
		assertNotNull(ReflectionUtil.getAppenderMethod(PMMLElements.OUTPUTFIELD_VALUES));
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
