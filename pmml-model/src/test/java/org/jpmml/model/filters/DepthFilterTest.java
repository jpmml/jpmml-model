/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.filters;

import javax.xml.bind.UnmarshalException;

import org.dmg.pmml.mining.Segmentation;
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DepthFilterTest {

	@Test
	public void filterNestedSegmentation() throws Exception {
		ResourceUtil.unmarshal(NestedSegmentationTest.class, new DepthFilter("Segmentation", 3));

		try {
			ResourceUtil.unmarshal(NestedSegmentationTest.class, new DepthFilter(Segmentation.class, 2));

			fail();
		} catch(UnmarshalException ue){
			Throwable cause = ue.getCause();

			assertTrue(cause instanceof SAXException);
		}

		ResourceUtil.unmarshal(NestedSegmentationTest.class, new DepthFilter("*", 15));

		try {
			ResourceUtil.unmarshal(NestedSegmentationTest.class, new DepthFilter("*", 10));

			fail();
		} catch(UnmarshalException ue){
			Throwable cause = ue.getCause();

			assertTrue(cause instanceof SAXException);
		}
	}
}