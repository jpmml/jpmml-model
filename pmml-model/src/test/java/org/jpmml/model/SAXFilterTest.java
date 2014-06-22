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

public class SAXFilterTest {

	@Test
	public void copy() throws Exception {
		Version[] versions = Version.values();

		for(Version version : versions){
			byte[] original = PMMLUtil.getResourceAsByteArray(version);

			assertTrue(checkVersion(original, version));

			Source source = ImportFilter.apply(new InputSource(new ByteArrayInputStream(original)));

			PMML pmml = JAXBUtil.unmarshalPMML(source);

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			JAXBUtil.marshalPMML(pmml, new StreamResult(buffer));

			byte[] latest = buffer.toByteArray();

			assertTrue(checkVersion(latest, Version.PMML_4_2));

			byte[] latestToOriginal = PMMLUtil.transform(latest, version);

			assertTrue(checkVersion(latestToOriginal, version));
		}
	}

	static
	private boolean checkVersion(byte[] bytes, Version version) throws IOException {
		String string = new String(bytes, "UTF-8");

		return string.contains("<PMML xmlns=\"" + version.getNamespaceURI() + "\" version=\"" + version.getVersion() + "\">");
	}
}