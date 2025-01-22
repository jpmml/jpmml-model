/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.filters;

import java.util.List;

import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.mining.MiningModel;
import org.dmg.pmml.mining.Segmentation;
import org.jpmml.model.ChainedSegmentationTest;
import org.jpmml.model.ExtensionUtil;
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.WildcardTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		PMML pmml = ResourceUtil.unmarshal(NestedSegmentationTest.class, new SkipFilter(Segmentation.class));

		assertNotNull(pmml.getDataDictionary());

		List<Model> models = pmml.getModels();

		MiningModel miningModel = (MiningModel)models.get(0);

		assertNotNull(miningModel.getMiningSchema());
		assertNotNull(miningModel.getLocalTransformations());
		assertNotNull(miningModel.getOutput());

		assertNull(miningModel.getSegmentation());
	}

	@Test
	public void filterCustomExtension() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(WildcardTest.class, new SkipFilter("http://localhost/test", "Extension"));

		assertTrue(pmml.hasExtensions());

		List<?> content = ExtensionUtil.getContent(pmml);

		assertEquals("First textSecond text", content.get(0));
	}
}