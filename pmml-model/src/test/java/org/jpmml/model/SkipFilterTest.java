/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.util.List;

import javax.xml.transform.Source;

import org.dmg.pmml.MiningModel;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.junit.Test;
import org.xml.sax.InputSource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SkipFilterTest {

	@Test
	public void chainedSegmentationTest() throws Exception {
		PMML pmml = loadFilteredResource(ChainedSegmentationTest.class);

		assertNotNull(pmml.getDataDictionary());
		assertNotNull(pmml.getTransformationDictionary());

		List<Model> models = pmml.getModels();

		MiningModel miningModel = (MiningModel)models.get(0);

		assertNotNull(miningModel.getMiningSchema());
		assertNotNull(miningModel.getOutput());

		assertNull(miningModel.getSegmentation());
	}

	@Test
	public void nestedSegmentationTest() throws Exception {
		PMML pmml = loadFilteredResource(NestedSegmentationTest.class);

		assertNotNull(pmml.getDataDictionary());

		List<Model> models = pmml.getModels();

		MiningModel miningModel = (MiningModel)models.get(0);

		assertNotNull(miningModel.getMiningSchema());
		assertNotNull(miningModel.getLocalTransformations());
		assertNotNull(miningModel.getOutput());

		assertNull(miningModel.getSegmentation());
	}

	static
	private PMML loadFilteredResource(Class<?> clazz) throws Exception {

		try(InputStream is = PMMLUtil.getResourceAsStream(clazz)){
			Source source = SkipFilter.apply(new InputSource(is), "Segmentation");

			return JAXBUtil.unmarshalPMML(source);
		}
	}
}