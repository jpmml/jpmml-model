/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.model;

import java.io.NotSerializableException;
import java.util.Arrays;
import java.util.List;

import org.dmg.pmml.Annotation;
import org.dmg.pmml.Extension;
import org.dmg.pmml.Header;
import org.dmg.pmml.PMML;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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