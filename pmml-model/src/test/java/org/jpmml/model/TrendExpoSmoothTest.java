/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.dmg.pmml.PMML;
import org.jpmml.schema.Version;
import org.junit.Test;
import org.xml.sax.InputSource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TrendExpoSmoothTest {

	@Test
	public void copy() throws Exception {
		String trendExpression = "/:PMML/:TimeSeriesModel/:ExponentialSmoothing/:Trend";
		String trendExpoSmoothExpression = "/:PMML/:TimeSeriesModel/:ExponentialSmoothing/:Trend_ExpoSmooth";

		byte[] original = PMMLUtil.getResourceAsByteArray(TrendExpoSmoothTest.class);

		assertNotNull(XPathUtil.selectNode(original, trendExpression));
		assertNull(XPathUtil.selectNode(original, trendExpoSmoothExpression));

		Source source = ImportFilter.apply(new InputSource(new ByteArrayInputStream(original)));

		PMML pmml = JAXBUtil.unmarshalPMML(source);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		JAXBUtil.marshalPMML(pmml, new StreamResult(buffer));

		byte[] latest = buffer.toByteArray();

		assertNull(XPathUtil.selectNode(latest, trendExpression));
		assertNotNull(XPathUtil.selectNode(latest, trendExpoSmoothExpression));

		byte[] latestToOriginal = PMMLUtil.transform(latest, Version.PMML_4_0);

		assertNotNull(XPathUtil.selectNode(latestToOriginal, trendExpression));
		assertNull(XPathUtil.selectNode(latestToOriginal, trendExpoSmoothExpression));
	}
}