/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.jpmml.model.visitors.LocatorNullifier;
import org.jpmml.model.visitors.LocatorTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SerializationUtilTest {

	@Test
	public void nullifyAndClone() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		assertTrue(pmml.hasLocator());

		try {
			SerializationUtil.clone(pmml);

			fail();
		} catch(NotSerializableException nse){
			// Ignored
		}

		pmml.accept(new LocatorNullifier());

		assertFalse(pmml.hasLocator());

		SerializationUtil.clone(pmml);
	}

	@Test
	public void transformAndClone() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		assertTrue(pmml.hasLocator());

		try {
			SerializationUtil.clone(pmml);

			fail();
		} catch(NotSerializableException nse){
			// Ignored
		}

		pmml.accept(new LocatorTransformer());

		assertTrue(pmml.hasLocator());

		SerializationUtil.clone(pmml);
	}

	@Test
	public void compressedSequence() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		pmml.accept(new LocatorNullifier());

		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(3 * 1024);

		try(OutputStream os = new GZIPOutputStream(buffer)){
			SerializationUtil.serializePMML(pmml, os);
			SerializationUtil.serializePMML(pmml, os);

			os.flush();
		}

		try(InputStream is = new GZIPInputStream(buffer.getInputStream())){
			PMML firstPmml = SerializationUtil.deserializePMML(is);
			PMML secondPmml = SerializationUtil.deserializePMML(is);

			assertNotSame(firstPmml, secondPmml);

			assertEquals(-1, is.read());
		}
	}
}