/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.*;

import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;

import org.dmg.pmml.*;

import org.junit.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import static org.junit.Assert.*;

public class SAXFilterTest {

	@Test
	public void copy() throws Exception {
		SAXTransformerFactory transformerFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();

		Version[] versions = Version.values();
		for(Version version : versions){
			ByteArrayOutputStream original = new ByteArrayOutputStream();

			InputStream is = PMMLUtil.getResourceAsStream(version);

			try {
				byte[] buffer = new byte[512];

				while(true){
					int count = is.read(buffer);
					if(count < 0){
						break;
					}

					original.write(buffer, 0, count);
				}
			} finally {
				is.close();
			}

			assertTrue(checkVersion(original, version));

			ImportFilter importFilter = new ImportFilter(XMLReaderFactory.createXMLReader());

			PMML pmml = JAXBUtil.unmarshalPMML(new SAXSource(importFilter, createInputSource(original)));

			ByteArrayOutputStream latest = new ByteArrayOutputStream();

			JAXBUtil.marshalPMML(pmml, new StreamResult(latest));

			assertTrue(checkVersion(latest, Version.PMML_4_2));

			ByteArrayOutputStream latestToOriginal = new ByteArrayOutputStream();

			TransformerHandler transformer = transformerFactory.newTransformerHandler();
			transformer.setResult(new StreamResult(latestToOriginal));

			ExportFilter exportFilter = new ExportFilter(XMLReaderFactory.createXMLReader(), version);
			exportFilter.setContentHandler(transformer);

			exportFilter.parse(createInputSource(latest));

			assertTrue(checkVersion(latestToOriginal, version));
		}
	}

	static
	private boolean checkVersion(ByteArrayOutputStream os, Version version) throws IOException {
		String string = os.toString("UTF-8");

		return string.contains("PMML xmlns=\"" + version.getNamespaceURI() + "\"");
	}

	static
	private InputSource createInputSource(ByteArrayOutputStream os){
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

		return new InputSource(is);
	}
}