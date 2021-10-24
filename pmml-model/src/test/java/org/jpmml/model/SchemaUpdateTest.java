/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import jakarta.xml.bind.JAXBException;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.jpmml.model.filters.ExportFilter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

abstract
public class SchemaUpdateTest {

	static
	public byte[] upgradeToLatest(byte[] bytes) throws IOException, JAXBException, SAXException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();

		try(InputStream is = new ByteArrayInputStream(bytes)){
			PMML pmml = PMMLUtil.unmarshal(is);

			PMMLUtil.marshal(pmml, result);
		}

		return result.toByteArray();
	}

	static
	public byte[] downgrade(byte[] bytes, Version version) throws IOException, TransformerConfigurationException, SAXException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();

		SAXTransformerFactory transformerFactory = (SAXTransformerFactory)TransformerFactory.newInstance();

		TransformerHandler transformer = transformerFactory.newTransformerHandler();
		transformer.setResult(new StreamResult(result));

		ExportFilter exportFilter = new ExportFilter(XMLReaderFactory.createXMLReader(), version);
		exportFilter.setContentHandler(transformer);

		try(InputStream is = new ByteArrayInputStream(bytes)){
			exportFilter.parse(new InputSource(is));
		}

		return result.toByteArray();
	}
}