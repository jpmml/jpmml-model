/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.model;

import java.io.NotSerializableException;
import java.util.Arrays;
import java.util.List;

import org.dmg.pmml.Extension;
import org.dmg.pmml.PMML;
import org.jpmml.model.visitors.LocatorTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MixedContentTest {

	@Test
	public void mixedContent() throws Exception {
		PMML pmml = PMMLUtil.loadResource(getClass());

		List<?> content = PMMLUtil.getExtension(pmml);

		assertEquals(5, content.size());

		assertEquals("First text value", content.get(0));
		assertEquals(Arrays.asList("First extension"), getDeepContent(content.get(1)));
		assertEquals("Second text value", content.get(2));
		assertEquals(Arrays.asList("Second extension"), getDeepContent(content.get(3)));
		assertEquals("Third text value", content.get(4));

		try {
			SerializationUtil.clone(pmml);

			fail();
		} catch(NotSerializableException nse){
			// Ignored
		}

		pmml.accept(new LocatorTransformer());

		SerializationUtil.clone(pmml);
	}

	static
	private List<?> getDeepContent(Object object){
		Extension extension = (Extension)object;

		return extension.getContent();
	}
}