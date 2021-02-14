/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.dmg.pmml.mining;

import org.jpmml.model.DOMUtil;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.SchemaUpdateTest;
import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WeightedSegmentationTest extends SchemaUpdateTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(WeightedSegmentationTest.class);

		checkSegmentation(original, "x-weightedSum", new String[]{"continue", null}, new String[]{"0", null});

		byte[] latest = upgradeToLatest(original);

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