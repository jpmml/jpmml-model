/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.dmg.pmml.PMML;
import org.jpmml.schema.Version;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import static org.junit.Assert.assertEquals;

public class TargetValueTest {

	@Test
	public void copy() throws Exception {
		byte[] original = PMMLUtil.getResourceAsByteArray(TargetValueTest.class);

		checkTargetValue(original, "", null);

		Source source = ImportFilter.apply(new InputSource(new ByteArrayInputStream(original)));

		PMML pmml = JAXBUtil.unmarshalPMML(source);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		JAXBUtil.marshalPMML(pmml, new StreamResult(buffer));

		byte[] latest = buffer.toByteArray();

		checkTargetValue(latest, null, "");

		byte[] latestToOriginal = PMMLUtil.transform(latest, Version.PMML_3_1);

		checkTargetValue(latestToOriginal, "", null);
	}

	static
	private void checkTargetValue(byte[] bytes, String rawDataValue, String displayValue) throws Exception {
		Node node = XPathUtil.selectNode(bytes, "/:PMML/:RegressionModel/:Targets/:Target/:TargetValue");

		assertEquals(rawDataValue, DOMUtil.getAttributeValue(node, "rawDataValue"));
		assertEquals(displayValue, DOMUtil.getAttributeValue(node, "displayValue"));
	}
}