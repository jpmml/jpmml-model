/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;

public class ResourceUtil {

	private ResourceUtil(){
	}

	static
	public PMML unmarshal(Class<?> clazz, XMLFilter... filters) throws IOException, SAXException, JAXBException {

		try(InputStream is = getStream(clazz)){
			Source source = SAXUtil.createFilteredSource(new InputSource(is), filters);

			return JAXBUtil.unmarshalPMML(source);
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

		if(!(Version.PMML_4_3).equals(version)){
			throw new IllegalArgumentException();
		}

		try(InputStream is = getStream(version)){
			return JAXBUtil.unmarshalPMML(new StreamSource(is));
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
		String namespaceUri = version.getNamespaceURI();
		String name = namespaceUri.substring(namespaceUri.lastIndexOf('/') + 1);

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