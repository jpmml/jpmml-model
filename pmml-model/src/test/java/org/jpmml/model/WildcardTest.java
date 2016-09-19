/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dmg.pmml.ObjectFactory;
import org.dmg.pmml.PMML;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WildcardTest {

	@Test
	public void domContent() throws Exception {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);

		List<?> content = getContent(context);

		assertEquals("First text", content.get(0));
		assertTrue(content.get(1) instanceof Element);
		assertEquals("Second text", content.get(2));
	}

	@Test
	public void jaxbContent() throws Exception {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class, LocalExtension.class);

		List<?> content = getContent(context);

		assertEquals("First text", content.get(0));
		assertTrue(content.get(1) instanceof LocalExtension);
		assertEquals("Second text", content.get(2));
	}

	private List<?> getContent(JAXBContext context) throws IOException, JAXBException {
		PMML pmml;

		Unmarshaller unmarshaller = context.createUnmarshaller();

		try(InputStream is = ResourceUtil.getStream(getClass())){
			pmml = (PMML)unmarshaller.unmarshal(is);
		}

		return ExtensionUtil.getContent(pmml);
	}
}