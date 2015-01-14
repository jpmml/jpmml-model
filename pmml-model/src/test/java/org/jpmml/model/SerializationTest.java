/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.NotSerializableException;

import org.dmg.pmml.PMML;
import org.jpmml.schema.Version;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class SerializationTest {

	@Test
	public void nullifyAndClone() throws Exception {
		PMML pmml = PMMLUtil.loadResource(Version.PMML_4_2);

		assertNotNull(pmml.getLocator());

		try {
			SerializationUtil.clone(pmml);

			fail();
		} catch(NotSerializableException nse){
		}

		pmml.accept(new SourceLocationNullifier());

		assertNull(pmml.getLocator());

		SerializationUtil.clone(pmml);
	}

	@Test
	public void transformAndClone() throws Exception {
		PMML pmml = PMMLUtil.loadResource(Version.PMML_4_2);

		assertNotNull(pmml.getLocator());

		try {
			SerializationUtil.clone(pmml);

			fail();
		} catch(NotSerializableException nse){
		}

		pmml.accept(new SourceLocationTransformer());

		assertNotNull(pmml.getLocator());

		SerializationUtil.clone(pmml);
	}
}