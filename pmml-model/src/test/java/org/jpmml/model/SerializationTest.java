/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.*;

import org.dmg.pmml.*;

import org.junit.*;

public class SerializationTest {

	@Test
	public void nullifyAndClone() throws Exception {
		PMML pmml = PMMLUtil.loadResource(Version.PMML_4_1);

		try {
			SerializationUtil.clone(pmml);

			Assert.fail();
		} catch(NotSerializableException nse){
		}

		pmml.accept(new SourceLocationNullifier());

		SerializationUtil.clone(pmml);
	}

	@Test
	public void transformAndClone() throws Exception {
		PMML pmml = PMMLUtil.loadResource(Version.PMML_4_1);

		try {
			SerializationUtil.clone(pmml);

			Assert.fail();
		} catch(NotSerializableException nse){
		}

		pmml.accept(new SourceLocationTransformer());

		SerializationUtil.clone(pmml);
	}
}