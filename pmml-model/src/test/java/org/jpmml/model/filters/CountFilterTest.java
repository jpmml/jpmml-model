/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.filters;

import jakarta.xml.bind.UnmarshalException;
import org.dmg.pmml.mining.Segmentation;
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.SAXUtil;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CountFilterTest {

	@Test
	public void filterNestedSegmentation() throws Exception {
		ResourceUtil.unmarshal(NestedSegmentationTest.class, new CountFilter("Segmentation", 3));

		try {
			ResourceUtil.unmarshal(NestedSegmentationTest.class, new CountFilter(Segmentation.class, 2));

			fail();
		} catch(UnmarshalException se){
			Throwable cause = SAXUtil.getCause(se);

			assertTrue(cause instanceof SAXException);
		}

		ResourceUtil.unmarshal(NestedSegmentationTest.class, new CountFilter("*", 100));

		try {
			ResourceUtil.unmarshal(NestedSegmentationTest.class, new CountFilter("*", 10));

			fail();
		} catch(UnmarshalException ue){
			Throwable cause = SAXUtil.getCause(ue);

			assertTrue(cause instanceof SAXException);
		}
	}
}