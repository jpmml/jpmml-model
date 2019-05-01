/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.jpmml.model.JAXBUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiningFieldTest {

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
	private void checkImportance(Number expected, String string) throws Exception {
		Reader reader = new StringReader("<MiningField xmlns=\"" + Version.PMML_4_3.getNamespaceURI() + "\" name=\"x\" importance=\"" + string + "\"/>");

		MiningField miningField = (MiningField)JAXBUtil.unmarshal(new StreamSource(reader));

		assertEquals(FieldName.create("x"), miningField.getName());
		assertEquals(MiningField.UsageType.ACTIVE, miningField.getUsageType());

		assertEquals(expected, miningField.getImportance());
	}
}