/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.PMML;
import org.jpmml.model.InvalidMarkupException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvalidMarkupInspectorTest {

	@Test
	public void inspect() throws Exception {
		DataDictionary dataDictionary = new DataDictionary()
			.setNumberOfFields(1);

		PMML pmml = new PMML(null, null, dataDictionary);

		InvalidMarkupInspector inspector = new InvalidMarkupInspector();

		InvalidMarkupException firstException = assertThrows(InvalidMarkupException.class, () -> inspector.applyTo(pmml));

		String message = firstException.getMessage();

		assertTrue(message.contains("DataDictionary"));

		List<InvalidMarkupException> exceptions = inspector.getExceptions();

		assertEquals(1, exceptions.size());
		assertEquals(0, exceptions.indexOf(firstException));
	}
}