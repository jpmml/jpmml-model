/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jakarta.xml.bind.JAXBElement;
import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.TextSerializer;
import org.jpmml.model.cells.InputCell;
import org.jpmml.model.cells.OutputCell;
import org.jpmml.model.resources.CellPolymorphismTest;
import org.jpmml.model.resources.ResourceUtil;
import org.jpmml.model.visitors.RowCleaner;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InlineTableTest {

	@Test
	public void unmarshal() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(CellPolymorphismTest.class);

		Row row = getRow(pmml);

		List<?> content = row.getContent();

		assertEquals(9, content.size());

		Visitor visitor = new RowCleaner();
		visitor.applyTo(pmml);

		assertEquals(4, content.size());

		Element first = (Element)content.get(0);
		assertEquals("any", first.getTagName());
		assertEquals("", first.getTextContent());

		InputCell second = (InputCell)content.get(1);
		assertEquals("0", second.getValue());

		OutputCell third = (OutputCell)content.get(2);
		assertEquals("zero", third.getValue());

		Element fourth = (Element)content.get(3);
		assertEquals("data:any", fourth.getTagName());
		assertEquals("", fourth.getTextContent());
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
			.addContent(new InputCell(0), new OutputCell(stringWrapper));

		checkRow(row);
	}

	@Test
	public void marshalDomElement() throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);

		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		Document document = documentBuilder.newDocument();

		Element inputElement = document.createElementNS(NamespaceURIs.JPMML_INLINETABLE, NamespacePrefixes.JPMML_INLINETABLE + ":input");
		inputElement.setTextContent("0");

		Element outputElement = document.createElementNS(NamespaceURIs.JPMML_INLINETABLE, NamespacePrefixes.JPMML_INLINETABLE + ":output");
		outputElement.setTextContent("zero");

		Row row = new Row()
			.addContent(inputElement, outputElement);

		checkRow(row);
	}

	@Test
	public void marshalJaxbElement() throws Exception {
		JAXBElement<?> inputElement = new JAXBElement<>(new QName(NamespaceURIs.JPMML_INLINETABLE, "input"), Integer.class, 0);
		JAXBElement<?> outputElement = new JAXBElement<>(new QName(NamespaceURIs.JPMML_INLINETABLE, "output"), String.class, "zero");

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
		TextSerializer serializer = new JAXBSerializer();

		InlineTable inlineTable = new InlineTable()
			.addRows(row);

		String string = SerializationUtil.toString(serializer, inlineTable);

		assertTrue(string.contains("<row>"));
		assertTrue(string.contains("<data:input>0</data:input>"));
		assertTrue(string.contains("<data:output>zero</data:output>"));
		assertTrue(string.contains("</row>"));
	}
}
