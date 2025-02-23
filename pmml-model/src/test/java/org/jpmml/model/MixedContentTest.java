/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.Collections;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import org.dmg.pmml.Extension;
import org.dmg.pmml.ObjectFactory;
import org.dmg.pmml.PMML;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MixedContentTest {

	@Test
	public void domContent() throws Exception {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);

		JAXBSerializer serializer = new JAXBSerializer(context);

		PMML pmml = ResourceUtil.unmarshal(serializer, MixedContentTest.class);

		assertTrue(pmml.hasLocator());

		List<?> content = ExtensionUtil.getContent(pmml);

		assertEquals(5, content.size());

		assertEquals("First text value", content.get(0));

		Element element = (Element)content.get(1);
		assertEquals("Custom extension", element.getTextContent());

		assertEquals("Second text value", content.get(2));

		Extension extension = (Extension)content.get(3);
		assertEquals(Collections.singletonList("Standard extension"), extension.getContent());

		assertEquals("Third text value", content.get(4));
	}

	@Test
	public void jaxbContent() throws Exception {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class, LocalExtension.class);

		JAXBSerializer serializer = new JAXBSerializer(context);

		PMML pmml = ResourceUtil.unmarshal(serializer, MixedContentTest.class);

		assertTrue(pmml.hasLocator());

		List<?> content = ExtensionUtil.getContent(pmml);

		assertEquals(5, content.size());

		assertEquals("First text value", content.get(0));

		LocalExtension localExtension = (LocalExtension)content.get(1);
		assertNotNull(localExtension);

		assertEquals("Second text value", content.get(2));

		Extension extension = (Extension)content.get(3);
		assertEquals(Collections.singletonList("Standard extension"), extension.getContent());

		assertEquals("Third text value", content.get(4));
	}
}