/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.jpmml.schema.Version;
import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

public class PMMLTest {

	@Test
	public void transform() throws Exception {
		Version[] versions = Version.values();

		for(Version version : versions){
			byte[] original = PMMLUtil.getResourceAsByteArray(version);

			checkPMML(original, version);

			byte[] latest = PMMLUtil.upgradeToLatest(original);

			checkPMML(latest, Version.PMML_4_2);

			byte[] latestToOriginal = PMMLUtil.downgrade(latest, version);

			checkPMML(latestToOriginal, version);
		}
	}

	static
	private void checkPMML(byte[] bytes, Version version) throws Exception {
		Node node = XPathUtil.selectNode(bytes, "/:PMML");

		assertEquals(version.getNamespaceURI(), node.getNamespaceURI());

		assertEquals(version.getVersion(), DOMUtil.getAttributeValue(node, "version"));
	}
}