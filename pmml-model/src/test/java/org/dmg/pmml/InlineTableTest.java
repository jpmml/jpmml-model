/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;

import org.jpmml.model.JAXBUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class InlineTableTest {

	@Test
	public void marshal() throws Exception {
		InlineTable inlineTable = new InlineTable();

		Row row = new Row();

		String namespaceURI = "http://jpmml.org/jpmml-model/InlineTable";

		row.addContent(new JAXBElement<>(new QName(namespaceURI, "zero"), String.class, "0"));
		row.addContent(new JAXBElement<>(new QName(namespaceURI, "one"), Integer.class, 1));

		inlineTable.addRows(row);

		String string;

		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			JAXBUtil.marshal(inlineTable, new StreamResult(os));

			string = os.toString("UTF-8");
		}

		assertTrue(string.contains("<row>"));
		assertTrue(string.contains("<data:zero>0</data:zero>"));
		assertTrue(string.contains("<data:one>1</data:one>"));
		assertTrue(string.contains("</row>"));
	}
}