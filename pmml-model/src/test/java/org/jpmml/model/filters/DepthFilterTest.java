/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.filters;

import jakarta.xml.bind.UnmarshalException;
import org.dmg.pmml.mining.Segmentation;
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.SAXUtil;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class DepthFilterTest {

	@Test
	public void filterNestedSegmentation() throws Exception {
		ResourceUtil.unmarshal(NestedSegmentationTest.class, new DepthFilter("Segmentation", 3));

		try {
			ResourceUtil.unmarshal(NestedSegmentationTest.class, new DepthFilter(Segmentation.class, 2));

			fail();
		} catch(UnmarshalException ue){
			Throwable cause = SAXUtil.getCause(ue);

			assertTrue(cause instanceof SAXException);
		}

		ResourceUtil.unmarshal(NestedSegmentationTest.class, new DepthFilter("*", 15));

		try {
			ResourceUtil.unmarshal(NestedSegmentationTest.class, new DepthFilter("*", 10));

			fail();
		} catch(UnmarshalException ue){
			Throwable cause = SAXUtil.getCause(ue);

			assertTrue(cause instanceof SAXException);
		}
	}
}