/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.List;

import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.mining.MiningModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SkipFilterTest {

	@Test
	public void filterChainedSegmentation() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(ChainedSegmentationTest.class, new SkipFilter("Segmentation"));

		assertNotNull(pmml.getDataDictionary());
		assertNotNull(pmml.getTransformationDictionary());

		List<Model> models = pmml.getModels();

		MiningModel miningModel = (MiningModel)models.get(0);

		assertNotNull(miningModel.getMiningSchema());
		assertNotNull(miningModel.getOutput());

		assertNull(miningModel.getSegmentation());
	}

	@Test
	public void filterNestedSegmentation() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(NestedSegmentationTest.class, new SkipFilter("Segmentation"));

		assertNotNull(pmml.getDataDictionary());

		List<Model> models = pmml.getModels();

		MiningModel miningModel = (MiningModel)models.get(0);

		assertNotNull(miningModel.getMiningSchema());
		assertNotNull(miningModel.getLocalTransformations());
		assertNotNull(miningModel.getOutput());

		assertNull(miningModel.getSegmentation());
	}

	@Test
	public void filterExtension() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(WildcardTest.class, new SkipFilter((String)null, "Extension"));

		assertFalse(pmml.hasExtensions());
	}

	@Test
	public void filterCustomExtension() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(WildcardTest.class, new SkipFilter("http://localhost/test", "Extension"));

		assertTrue(pmml.hasExtensions());

		List<?> content = ExtensionUtil.getContent(pmml);

		assertEquals("First textSecond text", content.get(0));
	}
}