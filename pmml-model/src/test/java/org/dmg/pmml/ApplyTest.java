/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import org.jpmml.model.DOMUtil;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.SchemaUpdateTest;
import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

public class ApplyTest extends SchemaUpdateTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(ApplyTest.class);

		checkApply(original, "", null);

		byte[] latest = upgradeToLatest(original);

		checkApply(latest, null, "");

		byte[] latestToOriginal = downgrade(latest, Version.PMML_4_1);

		checkApply(latestToOriginal, "", null);
	}

	static
	private void checkApply(byte[] bytes, String mapMissingTo, String defaultValue) throws Exception {
		Node node = DOMUtil.selectNode(bytes, "/:PMML/:TransformationDictionary/:DerivedField/:Apply");

		assertEquals(mapMissingTo, DOMUtil.getAttributeValue(node, "mapMissingTo"));
		assertEquals(defaultValue, DOMUtil.getAttributeValue(node, "defaultValue"));
	}
}