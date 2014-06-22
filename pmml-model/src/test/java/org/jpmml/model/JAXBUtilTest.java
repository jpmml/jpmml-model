/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dmg.pmml.CustomObjectFactory;
import org.dmg.pmml.CustomPMML;
import org.dmg.pmml.PMML;
import org.jpmml.schema.Version;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JAXBUtilTest {

	@Test
	public void copy() throws Exception {
		PMML pmml;

		InputStream is = PMMLUtil.getResourceAsStream(Version.PMML_4_2);

		try {
			pmml = JAXBUtil.unmarshalPMML(new StreamSource(is));
		} finally {
			is.close();
		}

		assertEquals(PMML.class, pmml.getClass());

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		JAXBUtil.marshalPMML(pmml, new StreamResult(os));

		assertTrue(os.size() > 0);
	}

	@Test
	public void copyCustom() throws Exception {
		PMML pmml;

		JAXBContext context = JAXBContext.newInstance(CustomObjectFactory.class);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory", new CustomObjectFactory());

		InputStream is = PMMLUtil.getResourceAsStream(Version.PMML_4_2);

		try {
			pmml = JAXBUtil.unmarshalPMML(unmarshaller, new StreamSource(is));
		} finally {
			is.close();
		}

		assertEquals(CustomPMML.class, pmml.getClass());

		Marshaller marshaller = context.createMarshaller();

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		JAXBUtil.marshalPMML(marshaller, pmml, new StreamResult(os));

		assertTrue(os.size() > 0);
	}
}