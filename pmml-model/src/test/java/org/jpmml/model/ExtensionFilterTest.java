/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.List;

import org.dmg.pmml.PMML;
import org.jpmml.schema.Version;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;

public class ExtensionFilterTest {

	@Test
	public void filter() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(ExtensionFilterTest.class, new ImportFilter());

		assertEquals("4.0", pmml.getBaseVersion());

		List<?> content = ExtensionUtil.getContent(pmml);

		assertEquals(2, content.size());

		Element parentElement = (Element)content.get(0);

		assertEquals(Version.PMML_4_3.getNamespaceURI(), parentElement.getNamespaceURI());
		assertEquals("X-Parent", parentElement.getLocalName());

		NodeList children = parentElement.getChildNodes();

		assertEquals(1, children.getLength());

		Element childElement = (Element)children.item(0);

		assertEquals(Version.PMML_4_3.getNamespaceURI(), childElement.getNamespaceURI());
		assertEquals("X-Child", childElement.getLocalName());

		Element testElement = (Element)content.get(1);

		assertEquals("http://localhost/test", testElement.getNamespaceURI());
		assertEquals("X-Test", testElement.getLocalName());

		pmml = ResourceUtil.unmarshal(ExtensionFilterTest.class, new ImportFilter(false));

		assertEquals(null, pmml.getBaseVersion());

		pmml = ResourceUtil.unmarshal(ExtensionFilterTest.class, new ImportFilter(), new ExtensionFilter());

		assertEquals(null, pmml.getBaseVersion());

		content = ExtensionUtil.getContent(pmml);

		assertEquals(1, content.size());

		testElement = (Element)content.get(0);

		assertEquals("http://localhost/test", testElement.getNamespaceURI());
		assertEquals("X-Test", testElement.getLocalName());
	}
}