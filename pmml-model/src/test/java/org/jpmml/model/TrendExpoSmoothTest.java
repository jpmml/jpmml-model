/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.jpmml.schema.Version;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TrendExpoSmoothTest {

	@Test
	public void transform() throws Exception {
		String trendExpression = "/:PMML/:TimeSeriesModel/:ExponentialSmoothing/:Trend";
		String trendExpoSmoothExpression = "/:PMML/:TimeSeriesModel/:ExponentialSmoothing/:Trend_ExpoSmooth";

		String extensionExpression = "/:PMML/:TimeSeriesModel/:Extension/test:Trend";

		byte[] original = PMMLUtil.getResourceAsByteArray(TrendExpoSmoothTest.class);

		assertNotNull(XPathUtil.selectNode(original, trendExpression));
		assertNull(XPathUtil.selectNode(original, trendExpoSmoothExpression));

		assertNotNull(XPathUtil.selectNode(original, extensionExpression));

		byte[] latest = PMMLUtil.upgradeToLatest(original);

		assertNull(XPathUtil.selectNode(latest, trendExpression));
		assertNotNull(XPathUtil.selectNode(latest, trendExpoSmoothExpression));

		assertNotNull(XPathUtil.selectNode(latest, extensionExpression));

		byte[] latestToOriginal = PMMLUtil.downgrade(latest, Version.PMML_4_0);

		assertNotNull(XPathUtil.selectNode(latestToOriginal, trendExpression));
		assertNull(XPathUtil.selectNode(latestToOriginal, trendExpoSmoothExpression));

		assertNotNull(XPathUtil.selectNode(latestToOriginal, extensionExpression));
	}
}