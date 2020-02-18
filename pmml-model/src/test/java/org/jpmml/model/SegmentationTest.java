/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model;

import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SegmentationTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(SegmentationTest.class);

		checkSegmentation(original, "x-weightedSum", new String[]{"continue", null}, new String[]{"0", null});

		byte[] latest = VersionUtil.upgradeToLatest(original);

		checkSegmentation(latest, "weightedSum", new String[]{null, "continue"}, new String[]{null, "0"});
	}

	static
	private void checkSegmentation(byte[] bytes, String multipleModelMethod, String[] missingPredictionTreatment, String[] missingThreshold) throws Exception {
		Node node = DOMUtil.selectNode(bytes, "/:PMML/:MiningModel/:Segmentation");

		assertEquals(multipleModelMethod, DOMUtil.getAttributeValue(node, "multipleModelMethod"));

		assertArrayEquals(missingPredictionTreatment, DOMUtil.getExtensionAttributeValues(node, "missingPredictionTreatment"));
		assertArrayEquals(missingThreshold, DOMUtil.getExtensionAttributeValues(node, "missingThreshold"));
	}
}