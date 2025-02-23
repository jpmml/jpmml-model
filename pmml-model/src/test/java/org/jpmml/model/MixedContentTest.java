/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.model;

import java.util.Arrays;
import java.util.List;

import org.dmg.pmml.Extension;
import org.dmg.pmml.PMML;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MixedContentTest {

	@Test
	public void mixedContent() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(getClass());

		assertTrue(pmml.hasLocator());

		List<?> content = ExtensionUtil.getContent(pmml);

		assertEquals(5, content.size());

		assertEquals("First text value", content.get(0));
		assertEquals(Arrays.asList("First extension"), getDeepContent(content.get(1)));
		assertEquals("Second text value", content.get(2));
		assertEquals(Arrays.asList("Second extension"), getDeepContent(content.get(3)));
		assertEquals("Third text value", content.get(4));
	}

	static
	private List<?> getDeepContent(Object object){
		Extension extension = (Extension)object;

		return extension.getContent();
	}
}