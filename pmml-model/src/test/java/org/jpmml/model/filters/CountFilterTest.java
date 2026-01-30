/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.filters;

import jakarta.xml.bind.UnmarshalException;
import org.dmg.pmml.mining.Segmentation;
import org.jpmml.model.SAXUtil;
import org.jpmml.model.resources.NestedSegmentationTest;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountFilterTest {

	@Test
	public void filterNestedSegmentation() throws Exception {
		ResourceUtil.unmarshal(NestedSegmentationTest.class, new CountFilter("Segmentation", 3));

		UnmarshalException exception = assertThrows(UnmarshalException.class, () -> ResourceUtil.unmarshal(NestedSegmentationTest.class, new CountFilter(Segmentation.class, 2)));

		assertInstanceOf(SAXException.class, SAXUtil.getCause(exception));

		ResourceUtil.unmarshal(NestedSegmentationTest.class, new CountFilter("*", 100));

		exception = assertThrows(UnmarshalException.class, () -> ResourceUtil.unmarshal(NestedSegmentationTest.class, new CountFilter("*", 10)));

		assertInstanceOf(SAXException.class, SAXUtil.getCause(exception));
	}
}