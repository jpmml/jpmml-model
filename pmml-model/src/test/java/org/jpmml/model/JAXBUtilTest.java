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
import javax.xml.validation.Schema;

import org.dmg.pmml.CustomObjectFactory;
import org.dmg.pmml.CustomPMML;
import org.dmg.pmml.PMML;
import org.jpmml.schema.Version;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JAXBUtilTest {

	@Test
	public void hasSchema() throws Exception {
		Schema schema = JAXBUtil.getSchema();

		assertNotNull(schema);
	}

	@Test
	public void copy() throws Exception {
		PMML pmml;

		try(InputStream is = ResourceUtil.getStream(Version.PMML_4_3)){
			pmml = JAXBUtil.unmarshalPMML(new StreamSource(is));
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

		try(InputStream is = ResourceUtil.getStream(Version.PMML_4_3)){
			pmml = (PMML)unmarshaller.unmarshal(is);
		}

		assertEquals(CustomPMML.class, pmml.getClass());

		Marshaller marshaller = context.createMarshaller();

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		marshaller.marshal(pmml, os);

		assertTrue(os.size() > 0);
	}
}