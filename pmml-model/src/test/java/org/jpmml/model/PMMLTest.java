/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.Version;
import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

public class PMMLTest {

	@Test
	public void transform() throws Exception {
		Version[] versions = Version.values();

		for(Version version : versions){

			if(!version.isStandard()){
				continue;
			}

			byte[] original = ResourceUtil.getByteArray(version);

			checkPMML(original, version);

			byte[] latest = VersionUtil.upgradeToLatest(original);

			checkPMML(latest, Version.PMML_4_4);

			byte[] latestToOriginal = VersionUtil.downgrade(latest, version);

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