/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.*;

import javax.xml.bind.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;

import org.dmg.pmml.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class PMMLUtil {

	private PMMLUtil(){
	}

	static
	public PMML loadResource(Class<?> clazz) throws IOException, JAXBException {
		InputStream is = getResourceAsStream(clazz);

		try {
			return JAXBUtil.unmarshalPMML(new StreamSource(is));
		} finally {
			is.close();
		}
	}

	static
	public byte[] getResourceAsByteArray(Class<?> clazz) throws IOException {
		InputStream is = getResourceAsStream(clazz);

		try {
			return toByteArray(is);
		} finally {
			is.close();
		}
	}

	static
	public InputStream getResourceAsStream(Class<?> clazz){
		String name = clazz.getSimpleName();

		return PMMLUtil.class.getResourceAsStream("/pmml/" + name + ".pmml");
	}

	static
	public PMML loadResource(Version version) throws IOException, JAXBException {
		InputStream is = getResourceAsStream(version);

		try {
			return JAXBUtil.unmarshalPMML(new StreamSource(is));
		} finally {
			is.close();
		}
	}

	static
	public byte[] getResourceAsByteArray(Version version) throws IOException {
		InputStream is = getResourceAsStream(version);

		try {
			return toByteArray(is);
		} finally {
			is.close();
		}
	}

	static
	public InputStream getResourceAsStream(Version version){
		String name = version.getNamespaceURI();
		name = name.substring(name.lastIndexOf('/') + 1);

		return PMMLUtil.class.getResourceAsStream("/pmml/" + name.toLowerCase() + ".pmml");
	}

	static
	public byte[] transform(byte[] bytes, Version version) throws IOException, TransformerConfigurationException, SAXException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();

		SAXTransformerFactory transformerFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();

		TransformerHandler transformer = transformerFactory.newTransformerHandler();
		transformer.setResult(new StreamResult(result));

		ExportFilter exportFilter = new ExportFilter(XMLReaderFactory.createXMLReader(), version);
		exportFilter.setContentHandler(transformer);

		InputStream is = new ByteArrayInputStream(bytes);

		try {
			exportFilter.parse(new InputSource(is));
		} finally {
			is.close();
		}

		return result.toByteArray();
	}

	static
	private byte[] toByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();

		byte[] buffer = new byte[512];

		while(true){
			int count = is.read(buffer);
			if(count < 0){
				break;
			}

			result.write(buffer, 0, count);
		}

		return result.toByteArray();
	}
}