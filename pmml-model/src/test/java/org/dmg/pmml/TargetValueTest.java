/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import org.jpmml.model.DOMUtil;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.SchemaUpdateTest;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TargetValueTest extends SchemaUpdateTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(TargetValueTest.class);

		checkTargetValue(original, "", null);

		byte[] latest = upgradeToLatest(original);

		checkTargetValue(latest, null, "");

		byte[] latestToOriginal = downgrade(latest, Version.PMML_3_1);

		checkTargetValue(latestToOriginal, "", null);
	}

	static
	private void checkTargetValue(byte[] bytes, String rawDataValue, String displayValue) throws Exception {
		Node node = DOMUtil.selectNode(bytes, "/:PMML/:RegressionModel/:Targets/:Target/:TargetValue");

		assertEquals(rawDataValue, DOMUtil.getAttributeValue(node, "rawDataValue"));
		assertEquals(displayValue, DOMUtil.getAttributeValue(node, "displayValue"));
	}
}