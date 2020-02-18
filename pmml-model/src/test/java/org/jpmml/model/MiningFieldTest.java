/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model;

import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

public class MiningFieldTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(MiningFieldTest.class);

		checkMiningField(original, "0", null);

		byte[] latest = VersionUtil.upgradeToLatest(original);

		checkMiningField(latest, null, "0");
	}

	static
	private void checkMiningField(byte[] bytes, String left, String right) throws Exception {
		Node node = DOMUtil.selectNode(bytes, "/:PMML/:RegressionModel/:MiningSchema/:MiningField");

		assertEquals(left, DOMUtil.getAttributeValue(node, "x-invalidValueReplacement"));
		assertEquals(right, DOMUtil.getAttributeValue(node, "invalidValueReplacement"));
	}
}