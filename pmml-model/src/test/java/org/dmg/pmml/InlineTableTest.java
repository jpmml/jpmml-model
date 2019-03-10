/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamResult;

import org.jpmml.model.JAXBUtil;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.inlinetable.InputCell;
import org.jpmml.model.inlinetable.OutputCell;
import org.jpmml.model.visitors.RowCleaner;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InlineTableTest {

	@Test
	public void unmarshal() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(InlineTableTest.class);

		Row row = getRow(pmml);

		List<?> content = row.getContent();

		assertEquals(9, content.size());

		Visitor visitor = new RowCleaner();
		visitor.applyTo(pmml);

		assertEquals(4, content.size());

		Object first = content.get(0);
		Object second = content.get(1);
		Object third = content.get(2);
		Object fourth = content.get(3);

		assertTrue(first instanceof Element);
		assertTrue(second instanceof InputCell);
		assertTrue(third instanceof OutputCell);
		assertTrue(fourth instanceof Element);

		assertEquals("0", ((InputCell)second).getValue());
		assertEquals("zero", ((OutputCell)third).getValue());
	}

	@Test
	public void marshalCell() throws Exception {
		ComplexValue stringWrapper = new ComplexValue(){

			@Override
			public Object toSimpleValue(){
				return "zero";
			}
		};

		Row row = new Row()
			.addContent(new InputCell(new Integer(0)), new OutputCell(stringWrapper));

		checkRow(row);
	}

	@Test
	public void marshalDomElement() throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);

		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		Document document = documentBuilder.newDocument();

		Element inputElement = document.createElementNS(XLMNS_DATA, "data:input");
		inputElement.setTextContent("0");

		Element outputElement = document.createElementNS(XLMNS_DATA, "data:output");
		outputElement.setTextContent("zero");

		Row row = new Row()
			.addContent(inputElement, outputElement);

		checkRow(row);
	}

	@Test
	public void marshalJaxbElement() throws Exception {
		JAXBElement<?> inputElement = new JAXBElement<>(new QName(XLMNS_DATA, "input"), Integer.class, 0);
		JAXBElement<?> outputElement = new JAXBElement<>(new QName(XLMNS_DATA, "output"), String.class, "zero");

		Row row = new Row()
			.addContent(inputElement, outputElement);

		checkRow(row);
	}

	static
	private Row getRow(PMML pmml){
		TransformationDictionary transformationDictionary = pmml.getTransformationDictionary();

		List<DerivedField> derivedFields = transformationDictionary.getDerivedFields();

		assertEquals(1, derivedFields.size());

		DerivedField derivedField = derivedFields.get(0);

		MapValues mapValues = (MapValues)derivedField.getExpression();

		InlineTable inlineTable = mapValues.getInlineTable();

		List<Row> rows = inlineTable.getRows();

		assertEquals(1, rows.size());

		return rows.get(0);
	}

	static
	private void checkRow(Row row) throws Exception {
		InlineTable inlineTable = new InlineTable()
			.addRows(row);

		String string;

		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			JAXBUtil.marshal(inlineTable, new StreamResult(os));

			string = os.toString("UTF-8");
		}

		assertTrue(string.contains("<row>"));
		assertTrue(string.contains("<data:input>0</data:input>"));
		assertTrue(string.contains("<data:output>zero</data:output>"));
		assertTrue(string.contains("</row>"));
	}

	private static final String XLMNS_DATA = "http://jpmml.org/jpmml-model/InlineTable";
}
