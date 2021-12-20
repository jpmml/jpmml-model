/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.PMML;
import org.jpmml.model.InvalidMarkupException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class InvalidMarkupInspectorTest {

	@Test
	public void inspect() throws Exception {
		DataDictionary dataDictionary = new DataDictionary()
			.setNumberOfFields(1);

		PMML pmml = new PMML(null, null, dataDictionary);

		InvalidMarkupInspector inspector = new InvalidMarkupInspector();

		try {
			inspector.applyTo(pmml);

			fail();
		} catch(InvalidMarkupException ime){
			List<InvalidMarkupException> exceptions = inspector.getExceptions();

			assertEquals(1, exceptions.size());

			String message = ime.getMessage();

			assertTrue(message.contains("DataDictionary"));
		}
	}
}