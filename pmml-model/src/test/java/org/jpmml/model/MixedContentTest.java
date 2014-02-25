/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.model;

import java.io.*;
import java.util.*;

import org.dmg.pmml.*;

import org.junit.*;

import static org.junit.Assert.*;

public class MixedContentTest {

	@Test
	public void mixedContent() throws Exception {
		PMML pmml = PMMLUtil.loadResource(getClass());

		Header header = pmml.getHeader();

		List<Annotation> annotations = header.getAnnotations();

		Annotation annotation = annotations.get(0);

		List<Object> content = annotation.getContent();

		assertEquals(5, content.size());

		assertEquals("First text value", content.get(0));
		assertEquals(Arrays.asList("First extension"), ((Extension)content.get(1)).getContent());
		assertEquals("Second text value", content.get(2));
		assertEquals(Arrays.asList("Second extension"), ((Extension)content.get(3)).getContent());
		assertEquals("Third text value", content.get(4));

		try {
			SerializationUtil.clone(pmml);

			Assert.fail();
		} catch(NotSerializableException nse){
		}

		pmml.accept(new SourceLocationTransformer());

		SerializationUtil.clone(pmml);
	}
}