/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.sun.xml.bind.v2.ContextFactory;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Header;
import org.dmg.pmml.PMML;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MarshallerTest {

	@Test
	public void marshal() throws Exception {
		PMML pmml = new PMML("4.3", new Header(), new DataDictionary());

		JAXBContext context = ContextFactory.createContext(new Class[]{org.dmg.pmml.ObjectFactory.class}, null);

		Marshaller marshaller = context.createMarshaller();

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		marshaller.marshal(pmml, os);

		String string = os.toString("UTF-8");

		assertTrue(string.contains("<PMML xmlns=\"http://www.dmg.org/PMML-4_3\" version=\"4.3\">"));
		assertTrue(string.contains("</PMML>"));
	}
}