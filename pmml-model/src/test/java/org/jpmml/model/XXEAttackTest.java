/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.UnmarshalException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.dmg.pmml.Extension;
import org.dmg.pmml.PMML;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class XXEAttackTest {

	@Test
	public void unmarshal() throws Exception {
		PMML pmml;

		InputStream is = PMMLUtil.getResourceAsStream(XXEAttackTest.class);

		System.setProperty("javax.xml.accessExternalDTD", "file");

		try {
			pmml = JAXBUtil.unmarshalPMML(new StreamSource(is));
		} finally {
			System.clearProperty("javax.xml.accessExternalDTD");

			is.close();
		}

		List<Extension> extensions = pmml.getExtensions();
		assertEquals(1, extensions.size());

		Extension extension = extensions.get(0);
		assertEquals(Arrays.asList("lol"), extension.getContent());
	}

	@Test
	public void unmarshalSecurely() throws Exception {
		InputStream is = PMMLUtil.getResourceAsStream(XXEAttackTest.class);

		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

			JAXBUtil.unmarshalPMML(new SAXSource(reader, new InputSource(is)));

			fail();
		} catch(NullPointerException npe){
			return;
		} catch(UnmarshalException ue){
			// Ignored
		} finally {
			is.close();
		}
	}
}