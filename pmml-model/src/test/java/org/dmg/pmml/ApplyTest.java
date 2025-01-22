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

public class ApplyTest extends SchemaUpdateTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(ApplyTest.class);

		checkApply(original, "x-" + PMMLFunctions.CONCAT, "", null);

		byte[] latest = upgradeToLatest(original);

		checkApply(latest, PMMLFunctions.CONCAT, null, "");

		byte[] latestToOriginal = downgrade(latest, Version.PMML_4_1);

		checkApply(latestToOriginal, "x-" + PMMLFunctions.CONCAT, "", null);
	}

	static
	private void checkApply(byte[] bytes, String function, String mapMissingTo, String defaultValue) throws Exception {
		Node node = DOMUtil.selectNode(bytes, "/:PMML/:TransformationDictionary/:DerivedField/:Apply");

		assertEquals(function, DOMUtil.getAttributeValue(node, "function"));

		assertEquals(mapMissingTo, DOMUtil.getAttributeValue(node, "mapMissingTo"));
		assertEquals(defaultValue, DOMUtil.getAttributeValue(node, "defaultValue"));
	}
}