/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import org.jpmml.model.DOMUtil;
import org.jpmml.model.SchemaUpdateTest;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PMMLTest extends SchemaUpdateTest {

	@Test
	public void transform() throws Exception {
		Version[] versions = Version.values();

		for(Version version : versions){

			if(!version.isStandard()){
				continue;
			}

			byte[] original = ResourceUtil.getByteArray(version);

			checkPMML(original, version);

			byte[] latest = upgradeToLatest(original);

			checkPMML(latest, Version.PMML_4_4);

			byte[] latestToOriginal = downgrade(latest, version);

			checkPMML(latestToOriginal, version);
		}
	}

	static
	private void checkPMML(byte[] bytes, Version version) throws Exception {
		Node node = DOMUtil.selectNode(bytes, "/:PMML");

		assertEquals(version.getNamespaceURI(), node.getNamespaceURI());

		assertEquals(version.getVersion(), DOMUtil.getAttributeValue(node, "version"));
	}
}