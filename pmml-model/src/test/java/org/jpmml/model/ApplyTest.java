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

public class ApplyTest {

	@Test
	public void copy() throws Exception {
		byte[] original = PMMLUtil.getResourceAsByteArray(ApplyTest.class);

		checkApply(original, "", null);

		Source source = ImportFilter.apply(new InputSource(new ByteArrayInputStream(original)));

		PMML pmml = JAXBUtil.unmarshalPMML(source);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		JAXBUtil.marshalPMML(pmml, new StreamResult(buffer));

		byte[] latest = buffer.toByteArray();

		checkApply(latest, null, "");

		byte[] latestToOriginal = PMMLUtil.transform(latest, Version.PMML_4_1);

		checkApply(latestToOriginal, "", null);
	}

	static
	private void checkApply(byte[] bytes, String mapMissingTo, String defaultValue) throws Exception {
		Node node = XPathUtil.selectNode(bytes, "/:PMML/:TransformationDictionary/:DerivedField/:Apply");

		assertEquals(mapMissingTo, DOMUtil.getAttributeValue(node, "mapMissingTo"));
		assertEquals(defaultValue, DOMUtil.getAttributeValue(node, "defaultValue"));
	}
}