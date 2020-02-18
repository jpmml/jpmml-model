/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model;

import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertArrayEquals;

public class MiningFieldTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(MiningFieldTest.class);

		checkMiningField(original, new String[]{"0", null});

		byte[] latest = VersionUtil.upgradeToLatest(original);

		checkMiningField(latest, new String[]{null, "0"});
	}

	static
	private void checkMiningField(byte[] bytes, String[] invalidValueReplacement) throws Exception {
		Node node = DOMUtil.selectNode(bytes, "/:PMML/:RegressionModel/:MiningSchema/:MiningField");

		assertArrayEquals(invalidValueReplacement, DOMUtil.getExtensionAttributeValues(node, "invalidValueReplacement"));
	}
}