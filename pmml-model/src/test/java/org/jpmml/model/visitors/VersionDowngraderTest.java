/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.Apply;
import org.dmg.pmml.ClusteringModelQuality;
import org.dmg.pmml.Extension;
import org.dmg.pmml.Header;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.ModelExplanation;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.TargetValue;
import org.dmg.pmml.Version;
import org.dmg.pmml.clustering.ClusteringModel;
import org.dmg.pmml.clustering.PMMLAttributes;
import org.dmg.pmml.time_series.TrendExpoSmooth;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.UnsupportedAttributeException;
import org.jpmml.model.UnsupportedElementException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class VersionDowngraderTest {

	@Test
	public void downgradeApply(){
		Apply apply = new Apply()
			.setDefaultValue(0)
			.setMapMissingTo(null);

		apply = downgrade(apply, Version.PMML_4_2);

		assertEquals(0, apply.getDefaultValue());
		assertNull(apply.getMapMissingTo());

		apply = downgrade(apply, Version.PMML_4_1);

		assertNull(apply.getDefaultValue());
		assertEquals(0, apply.getMapMissingTo());

		apply = new Apply()
			.setDefaultValue(0)
			.setMapMissingTo(-999);

		apply = downgrade(apply, Version.PMML_4_2);

		assertEquals(0, apply.getDefaultValue());
		assertEquals(-999, apply.getMapMissingTo());

		try {
			downgrade(apply, Version.PMML_4_1);

			fail();
		} catch(UnsupportedAttributeException uae){
			// Ignored
		}
	}

	@Test
	public void downgradeMiningField(){
		MiningField miningField = new MiningField()
			.setUsageType(MiningField.UsageType.TARGET);

		miningField = downgrade(miningField, Version.PMML_4_2);

		assertEquals(MiningField.UsageType.TARGET, miningField.getUsageType());

		miningField = downgrade(miningField, Version.PMML_4_1);

		assertEquals(MiningField.UsageType.PREDICTED, miningField.getUsageType());
	}

	@Test
	public void downgradePMML(){
		Extension extension = new Extension();

		ClusteringModelQuality clusteringModelQuality = new ClusteringModelQuality()
			.addExtensions(extension);

		ModelExplanation modelExplanation = new ModelExplanation()
			.addClusteringModelQualities(clusteringModelQuality);

		ClusteringModel clusteringModel = new ClusteringModel()
			.setScorable(false)
			.setModelExplanation(modelExplanation);

		Header header = new Header()
			.setModelVersion("1.0");

		PMML pmml = new PMML()
			.setHeader(header)
			.addModels(clusteringModel);

		pmml = downgrade(pmml, Version.PMML_4_4);

		assertTrue(clusteringModelQuality.hasExtensions());

		assertEquals("4.4", pmml.getVersion());

		pmml = downgrade(pmml, Version.PMML_4_3);

		assertFalse(clusteringModelQuality.hasExtensions());

		assertNotNull(header.getModelVersion());

		pmml = downgrade(pmml, Version.PMML_4_1);

		assertNull(header.getModelVersion());

		assertFalse(clusteringModel.isScorable());

		pmml = downgrade(pmml, Version.PMML_4_0);

		assertTrue(clusteringModel.isScorable());

		assertNull(ReflectionUtil.getFieldValue(PMMLAttributes.CLUSTERINGMODEL_SCORABLE, clusteringModel));

		assertNotNull(clusteringModel.getModelExplanation());

		pmml = downgrade(pmml, Version.PMML_3_2);

		assertNull(clusteringModel.getModelExplanation());

		assertEquals("3.2", pmml.getVersion());
	}

	@Test
	public void downgradeTargetValue(){
		TargetValue targetValue = new TargetValue()
			.setValue(1)
			.setDisplayValue("one");

		targetValue = downgrade(targetValue, Version.PMML_4_0);

		assertEquals(1, targetValue.getValue());
		assertEquals("one", targetValue.getDisplayValue());

		try {
			downgrade(targetValue, Version.PMML_3_2);

			fail();
		} catch(UnsupportedAttributeException uae){
			// Ignored
		}
	}

	@Test
	public void downgradeTrendExpoSmooth(){
		TrendExpoSmooth trendExpoSmooth = new TrendExpoSmooth();

		trendExpoSmooth = downgrade(trendExpoSmooth, Version.PMML_4_1);

		try {
			downgrade(trendExpoSmooth, Version.PMML_4_0);
		} catch(UnsupportedElementException uee){
			// Ignored
		}
	}

	static
	private <E extends PMMLObject> E downgrade(E object, Version version){
		VersionDowngrader inspector = new VersionDowngrader(version);
		inspector.applyTo(object);

		return object;
	}
}