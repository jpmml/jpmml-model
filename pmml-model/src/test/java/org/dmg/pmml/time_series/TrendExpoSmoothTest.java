/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml.time_series;

import org.dmg.pmml.Version;
import org.jpmml.model.DOMUtil;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.SchemaUpdateTest;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TrendExpoSmoothTest extends SchemaUpdateTest {

	@Test
	public void transform() throws Exception {
		String trendExpression = "/:PMML/:TimeSeriesModel/:ExponentialSmoothing/:Trend";
		String trendExpoSmoothExpression = "/:PMML/:TimeSeriesModel/:ExponentialSmoothing/:Trend_ExpoSmooth";

		String extensionExpression = "/:PMML/:TimeSeriesModel/:Extension/test:Trend";

		byte[] original = ResourceUtil.getByteArray(TrendExpoSmoothTest.class);

		assertNotNull(DOMUtil.selectNode(original, trendExpression));
		assertNull(DOMUtil.selectNode(original, trendExpoSmoothExpression));

		assertNotNull(DOMUtil.selectNode(original, extensionExpression));

		byte[] latest = upgradeToLatest(original);

		assertNull(DOMUtil.selectNode(latest, trendExpression));
		assertNotNull(DOMUtil.selectNode(latest, trendExpoSmoothExpression));

		assertNotNull(DOMUtil.selectNode(latest, extensionExpression));

		byte[] latestToOriginal = downgrade(latest, Version.PMML_4_0);

		assertNotNull(DOMUtil.selectNode(latestToOriginal, trendExpression));
		assertNull(DOMUtil.selectNode(latestToOriginal, trendExpoSmoothExpression));

		assertNotNull(DOMUtil.selectNode(latestToOriginal, extensionExpression));
	}
}