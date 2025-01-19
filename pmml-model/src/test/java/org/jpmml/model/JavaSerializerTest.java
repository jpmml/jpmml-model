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

public class JavaSerializerTest extends SerializerTest {

	@Test
	public void nonLocatableClone() throws Exception {
		Serializer serializer = new JavaSerializer();

		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		assertTrue(pmml.hasLocator());

		try {
			clone(serializer, pmml);

			fail();
		} catch(NotSerializableException nse){
			// Ignored
		}

		pmml.accept(new LocatorNullifier());

		assertFalse(pmml.hasLocator());

		PMML clonedPmml = checkedClone(serializer, pmml);

		assertFalse(clonedPmml.hasLocator());
	}

	@Test
	public void locatableClone() throws Exception {
		Serializer serializer = new JavaSerializer();

		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		assertTrue(pmml.hasLocator());

		try {
			clone(serializer, pmml);

			fail();
		} catch(NotSerializableException nse){
			// Ignored
		}

		pmml.accept(new LocatorTransformer());

		assertTrue(pmml.hasLocator());

		PMML clonedPmml = checkedClone(serializer, pmml);

		assertTrue(clonedPmml.hasLocator());
	}

	@Test
	public void compressedSequenceClone() throws Exception {
		Serializer serializer = new JavaSerializer();

		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		pmml.accept(new LocatorNullifier());

		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(10 * 1024);

		try(OutputStream os = new GZIPOutputStream(buffer)){
			serializer.serialize(pmml, os);
			serializer.serialize(pmml, os);

			os.flush();
		}

		try(InputStream is = new GZIPInputStream(buffer.getInputStream())){
			PMML firstPmml = (PMML)serializer.deserialize(is);
			PMML secondPmml = (PMML)serializer.deserialize(is);

			assertEquals(-1, is.read());

			assertTrue(ReflectionUtil.equals(pmml, firstPmml));
			assertTrue(ReflectionUtil.equals(pmml, secondPmml));

			assertNotSame(firstPmml, secondPmml);

			assertFalse(firstPmml.hasLocator());
			assertFalse(secondPmml.hasLocator());
		}
	}
}