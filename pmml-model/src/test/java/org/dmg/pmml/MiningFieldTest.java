/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.jpmml.model.DOMUtil;
import org.jpmml.model.JAXBUtil;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.SchemaUpdateTest;
import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MiningFieldTest extends SchemaUpdateTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(MiningFieldTest.class);

		checkMiningField(original, "asIs", new String[]{"0", null});

		byte[] latest = upgradeToLatest(original);

		checkMiningField(latest, "asValue", new String[]{null, "0"});

		byte[] latestToOriginal = downgrade(latest, Version.PMML_4_3);

		checkMiningField(latestToOriginal, "asIs", new String[]{"0", null});
	}

	@Test
	public void unmarshal() throws Exception {
		checkImportance(-100, "-100");

		checkImportance(0, "0");
		checkImportance(0.0d, "0.0");

		checkImportance(1, "1");
		checkImportance(1.0d, "1.0");

		checkImportance(100, "100");
	}

	static
	private void checkMiningField(byte[] bytes, String invalidValueTreatment, String[] invalidValueReplacement) throws Exception {
		Node node = DOMUtil.selectNode(bytes, "/:PMML/:RegressionModel/:MiningSchema/:MiningField");

		assertEquals(invalidValueTreatment, DOMUtil.getAttributeValue(node, "invalidValueTreatment"));
		assertArrayEquals(invalidValueReplacement, DOMUtil.getExtensionAttributeValues(node, "invalidValueReplacement"));
	}

	static
	private void checkImportance(Number expected, String string) throws Exception {
		Reader reader = new StringReader("<MiningField xmlns=\"" + Version.PMML_4_4.getNamespaceURI() + "\" name=\"x\" importance=\"" + string + "\"/>");

		MiningField miningField = (MiningField)JAXBUtil.unmarshal(new StreamSource(reader));

		assertEquals("x", miningField.getName());
		assertEquals(MiningField.UsageType.ACTIVE, miningField.getUsageType());

		assertEquals(expected, miningField.getImportance());
	}
}