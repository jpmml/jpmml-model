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

public class SAXFilterTest {

	@Test
	public void copy() throws Exception {
		Version[] versions = Version.values();

		for(Version version : versions){
			byte[] original = PMMLUtil.getResourceAsByteArray(version);

			checkPMML(original, version);

			Source source = ImportFilter.apply(new InputSource(new ByteArrayInputStream(original)));

			PMML pmml = JAXBUtil.unmarshalPMML(source);

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			JAXBUtil.marshalPMML(pmml, new StreamResult(buffer));

			byte[] latest = buffer.toByteArray();

			checkPMML(latest, Version.PMML_4_2);

			byte[] latestToOriginal = PMMLUtil.transform(latest, version);

			checkPMML(latestToOriginal, version);
		}
	}

	static
	private void checkPMML(byte[] bytes, Version version) throws Exception {
		Node node = XPathUtil.selectNode(bytes, "/:PMML");

		assertEquals(version.getNamespaceURI(), node.getNamespaceURI());

		assertEquals(version.getVersion(), DOMUtil.getAttributeValue(node, "version"));
	}
}