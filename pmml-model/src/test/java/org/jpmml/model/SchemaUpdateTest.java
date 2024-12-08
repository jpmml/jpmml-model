/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;

import jakarta.xml.bind.JAXBException;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.jpmml.model.filters.ExportFilter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

abstract
public class SchemaUpdateTest {

	static
	public byte[] upgradeToLatest(byte[] bytes) throws IOException, ParserConfigurationException, SAXException, JAXBException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();

		try(InputStream is = new ByteArrayInputStream(bytes)){
			PMML pmml = PMMLUtil.unmarshal(is);

			PMMLUtil.marshal(pmml, result);
		}

		return result.toByteArray();
	}

	static
	public byte[] downgrade(byte[] bytes, Version version) throws IOException, TransformerConfigurationException, ParserConfigurationException, SAXException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();

		try(InputStream is = new ByteArrayInputStream(bytes)){
			SAXUtil.transform(new InputSource(is), new StreamResult(result), new ExportFilter(version));
		}

		return result.toByteArray();
	}
}