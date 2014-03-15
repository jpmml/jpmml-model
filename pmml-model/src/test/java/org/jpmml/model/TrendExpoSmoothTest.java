/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.jpmml.schema.*;

import org.dmg.pmml.*;

import org.junit.*;

import org.xml.sax.*;

import static org.junit.Assert.*;

public class TrendExpoSmoothTest {

	@Test
	public void copy() throws Exception {
		byte[] original = PMMLUtil.getResourceAsByteArray(TrendExpoSmoothTest.class);

		assertTrue(checkElement(original, "Trend"));

		Source source = ImportFilter.apply(new InputSource(new ByteArrayInputStream(original)));

		PMML pmml = JAXBUtil.unmarshalPMML(source);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		JAXBUtil.marshalPMML(pmml, new StreamResult(buffer));

		byte[] latest = buffer.toByteArray();

		assertTrue(checkElement(latest, "Trend_ExpoSmooth"));
		assertFalse(checkElement(latest, "Trend"));

		byte[] latestToOriginal = PMMLUtil.transform(latest, Version.PMML_4_0);

		assertTrue(checkElement(latestToOriginal, "Trend"));
	}

	static
	private boolean checkElement(byte[] bytes, String tag) throws IOException {
		String string = new String(bytes, "UTF-8");

		return string.contains("<" + tag + "/>");
	}
}