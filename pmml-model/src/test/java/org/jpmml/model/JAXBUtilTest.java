/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.*;

import javax.xml.bind.*;
import javax.xml.transform.stream.*;

import org.dmg.pmml.*;

import org.junit.*;

import static org.junit.Assert.*;

public class JAXBUtilTest {

	@Test
	public void copy() throws Exception {
		InputStream is = getEmptyDocumentAsStream();

		PMML pmml = JAXBUtil.unmarshalPMML(new StreamSource(is));

		assertEquals(PMML.class, pmml.getClass());

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		JAXBUtil.marshalPMML(pmml, new StreamResult(os));

		assertTrue(os.size() > 0);
	}

	@Test
	public void copyCustom() throws Exception {
		InputStream is = getEmptyDocumentAsStream();

		JAXBContext context = JAXBContext.newInstance(CustomObjectFactory.class);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory", new CustomObjectFactory());

		PMML pmml = JAXBUtil.unmarshalPMML(unmarshaller, new StreamSource(is));

		assertEquals(CustomPMML.class, pmml.getClass());

		Marshaller marshaller = context.createMarshaller();

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		JAXBUtil.marshalPMML(marshaller, pmml, new StreamResult(os));

		assertTrue(os.size() > 0);
	}

	static
	private InputStream getEmptyDocumentAsStream(){
		return JAXBUtilTest.class.getResourceAsStream("/pmml/empty.pmml");
	}
}