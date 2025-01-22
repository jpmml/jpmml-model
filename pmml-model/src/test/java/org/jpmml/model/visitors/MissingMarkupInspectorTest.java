/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.PMML;
import org.jpmml.model.MissingMarkupException;
import org.jpmml.model.ReflectionUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MissingMarkupInspectorTest {

	@Test
	public void inspect(){
		DataDictionary dataDictionary = new DataDictionary();

		Field field = ReflectionUtil.getField(DataDictionary.class, "dataFields");

		assertNull(ReflectionUtil.getFieldValue(field, dataDictionary));

		List<DataField> dataFields = dataDictionary.getDataFields();
		assertEquals(0, dataFields.size());

		assertNotNull(ReflectionUtil.getFieldValue(field, dataDictionary));

		PMML pmml = new PMML(null, null, dataDictionary);

		MissingMarkupInspector inspector = new MissingMarkupInspector();

		try {
			inspector.applyTo(pmml);

			fail();
		} catch(MissingMarkupException mme){
			List<MissingMarkupException> exceptions = inspector.getExceptions();

			String[] features = {"PMML@version", "PMML/Header"};

			assertEquals(features.length, exceptions.size());
			assertEquals(0, exceptions.indexOf(mme));

			for(int i = 0; i < exceptions.size(); i++){
				MissingMarkupException exception = exceptions.get(i);

				String message = exception.getMessage();

				assertTrue(message.contains(features[i]));
			}
		}
	}
}