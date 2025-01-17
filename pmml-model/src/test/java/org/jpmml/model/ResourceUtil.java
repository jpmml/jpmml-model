/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBException;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;

public class ResourceUtil {

	private ResourceUtil(){
	}

	static
	public PMML unmarshal(Class<?> clazz, XMLFilter... filters) throws IOException, ParserConfigurationException, SAXException, JAXBException {

		try(InputStream is = getStream(clazz)){
			JAXBSerializer serializer = new JAXBSerializer(JAXBUtil.getContext());

			Source source = SAXUtil.createFilteredSource(is, filters);

			return (PMML)serializer.unmarshal(source);
		}
	}

	static
	public byte[] getByteArray(Class<?> clazz) throws IOException {

		try(InputStream is = getStream(clazz)){
			return toByteArray(is);
		}
	}

	static
	public InputStream getStream(Class<?> clazz){
		return getResourceAsStream(clazz.getSimpleName());
	}

	static
	public PMML unmarshal(Version version) throws IOException, JAXBException {

		if(!(Version.PMML_4_4).equals(version)){
			throw new IllegalArgumentException();
		}

		try(InputStream is = getStream(version)){
			JAXBSerializer serializer = new JAXBSerializer(JAXBUtil.getContext());

			Source source = new StreamSource(is);

			return (PMML)serializer.unmarshal(source);
		}
	}

	static
	public byte[] getByteArray(Version version) throws IOException {

		try(InputStream is = getStream(version)){
			return toByteArray(is);
		}
	}

	static
	public InputStream getStream(Version version){
		String namespaceURI = version.getNamespaceURI();

		String name = namespaceURI.substring(namespaceURI.lastIndexOf('/') + 1);

		return getResourceAsStream(name.toLowerCase());
	}

	static
	private InputStream getResourceAsStream(String name){
		return PMMLUtil.class.getResourceAsStream("/pmml/" + name + ".pmml");
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