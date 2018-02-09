/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.Version;
import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

public class TargetValueTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(TargetValueTest.class);

		checkTargetValue(original, "", null);

		byte[] latest = VersionUtil.upgradeToLatest(original);

		checkTargetValue(latest, null, "");

		byte[] latestToOriginal = VersionUtil.downgrade(latest, Version.PMML_3_1);

		checkTargetValue(latestToOriginal, "", null);
	}

	static
	private void checkTargetValue(byte[] bytes, String rawDataValue, String displayValue) throws Exception {
		Node node = XPathUtil.selectNode(bytes, "/:PMML/:RegressionModel/:Targets/:Target/:TargetValue");

		assertEquals(rawDataValue, DOMUtil.getAttributeValue(node, "rawDataValue"));
		assertEquals(displayValue, DOMUtil.getAttributeValue(node, "displayValue"));
	}
}