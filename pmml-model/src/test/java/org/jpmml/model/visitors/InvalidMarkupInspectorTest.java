/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.PMML;
import org.jpmml.model.InvalidMarkupException;
import org.jpmml.model.ReflectionUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class InvalidMarkupInspectorTest {

	@Test
	public void inspect() throws Exception {
		DataDictionary dataDictionary = new DataDictionary()
			.setNumberOfFields(1);

		Field field = ReflectionUtil.getField(DataDictionary.class, "dataFields");

		assertNull(ReflectionUtil.getFieldValue(field, dataDictionary));

		List<DataField> dataFields = dataDictionary.getDataFields();
		assertEquals(0, dataFields.size());

		assertNotNull(ReflectionUtil.getFieldValue(field, dataDictionary));

		PMML pmml = new PMML(null, null, dataDictionary);

		InvalidMarkupInspector inspector = new InvalidMarkupInspector();

		try {
			inspector.applyTo(pmml);

			fail();
		} catch(InvalidMarkupException ime){
			List<InvalidMarkupException> exceptions = inspector.getExceptions();

			String[] features = {"PMML@version", "PMML/Header", "DataDictionary"};

			assertEquals(features.length, exceptions.size());
			assertEquals(0, exceptions.indexOf(ime));

			for(int i = 0; i < exceptions.size(); i++){
				InvalidMarkupException exception = exceptions.get(i);

				String message = exception.getMessage();

				assertTrue(message.contains(features[i]));
			}
		}
	}
}