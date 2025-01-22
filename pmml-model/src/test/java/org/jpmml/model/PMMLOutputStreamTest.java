/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PMMLOutputStreamTest {

	@Test
	public void marshal() throws Exception {
		PMML pmml = new PMML();

		try {
			marshal(pmml, Version.XPMML);

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}

		String string = marshal(pmml, Version.PMML_4_4);

		assertTrue(string.contains(Version.PMML_4_4.getNamespaceURI()));
		assertFalse(string.contains(Version.PMML_4_3.getNamespaceURI()));

		string = marshal(pmml, Version.PMML_4_3);

		assertFalse(string.contains(Version.PMML_4_4.getNamespaceURI()));
		assertTrue(string.contains(Version.PMML_4_3.getNamespaceURI()));
	}

	static
	private String marshal(PMML pmml, Version version) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		try(OutputStream os = new PMMLOutputStream(buffer, version)){
			Serializer serializer = new JAXBSerializer();

			serializer.serialize(pmml, os);
		}

		return buffer.toString("UTF-8");
	}
}