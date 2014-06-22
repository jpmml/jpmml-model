/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.dmg.pmml.PMML;
import org.jpmml.schema.Version;
import org.junit.Test;
import org.xml.sax.InputSource;

import static org.junit.Assert.assertTrue;

public class ApplyTest {

	@Test
	public void copy() throws Exception {
		byte[] original = PMMLUtil.getResourceAsByteArray(ApplyTest.class);

		assertTrue(checkAttribute(original, "mapMissingTo"));

		Source source = ImportFilter.apply(new InputSource(new ByteArrayInputStream(original)));

		PMML pmml = JAXBUtil.unmarshalPMML(source);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		JAXBUtil.marshalPMML(pmml, new StreamResult(buffer));

		byte[] latest = buffer.toByteArray();

		assertTrue(checkAttribute(latest, "defaultValue"));

		byte[] latestToOriginal = PMMLUtil.transform(latest, Version.PMML_4_1);

		assertTrue(checkAttribute(latestToOriginal, "mapMissingTo"));
	}

	static
	private boolean checkAttribute(byte[] bytes, String name) throws IOException {
		String string = new String(bytes, "UTF-8");

		return string.contains("<Apply function=\"concat\" " + name + "=\"\">");
	}
}