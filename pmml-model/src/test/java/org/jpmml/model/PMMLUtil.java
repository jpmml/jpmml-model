/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dmg.pmml.Extension;
import org.dmg.pmml.PMML;
import org.jpmml.schema.Version;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import static org.junit.Assert.assertEquals;

public class PMMLUtil {

	private PMMLUtil(){
	}

	static
	public List<?> getExtension(PMML pmml){
		List<Extension> extensions = pmml.getExtensions();

		assertEquals(1, extensions.size());

		Extension extension = extensions.get(0);

		return extension.getContent();
	}

	static
	public PMML loadResource(Class<?> clazz) throws IOException, JAXBException {

		try(InputStream is = getResourceAsStream(clazz)){
			return JAXBUtil.unmarshalPMML(new StreamSource(is));
		}
	}

	static
	public byte[] getResourceAsByteArray(Class<?> clazz) throws IOException {

		try(InputStream is = getResourceAsStream(clazz)){
			return toByteArray(is);
		}
	}

	static
	public InputStream getResourceAsStream(Class<?> clazz){
		String name = clazz.getSimpleName();

		return PMMLUtil.class.getResourceAsStream("/pmml/" + name + ".pmml");
	}

	static
	public PMML loadResource(Version version) throws IOException, JAXBException {

		try(InputStream is = getResourceAsStream(version)){
			return JAXBUtil.unmarshalPMML(new StreamSource(is));
		}
	}

	static
	public byte[] getResourceAsByteArray(Version version) throws IOException {

		try(InputStream is = getResourceAsStream(version)){
			return toByteArray(is);
		}
	}

	static
	public InputStream getResourceAsStream(Version version){
		String namespaceUri = version.getNamespaceURI();
		String name = namespaceUri.substring(namespaceUri.lastIndexOf('/') + 1);

		return PMMLUtil.class.getResourceAsStream("/pmml/" + name.toLowerCase() + ".pmml");
	}

	static
	public byte[] upgradeToLatest(byte[] bytes) throws IOException, JAXBException, SAXException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();

		try(InputStream is = new ByteArrayInputStream(bytes)){
			Source source = ImportFilter.apply(new InputSource(is));

			PMML pmml = JAXBUtil.unmarshalPMML(source);

			JAXBUtil.marshalPMML(pmml, new StreamResult(result));
		}

		return result.toByteArray();
	}

	static
	public byte[] downgrade(byte[] bytes, Version version) throws IOException, TransformerConfigurationException, SAXException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();

		SAXTransformerFactory transformerFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();

		TransformerHandler transformer = transformerFactory.newTransformerHandler();
		transformer.setResult(new StreamResult(result));

		ExportFilter exportFilter = new ExportFilter(XMLReaderFactory.createXMLReader(), version);
		exportFilter.setContentHandler(transformer);

		try(InputStream is = new ByteArrayInputStream(bytes)){
			exportFilter.parse(new InputSource(is));
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