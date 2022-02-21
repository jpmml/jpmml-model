/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import jakarta.xml.bind.JAXBException;
import org.dmg.pmml.PMML;
import org.jpmml.model.filters.ImportFilter;
import org.xml.sax.SAXException;

public class PMMLUtil {

	private PMMLUtil(){
	}

	/**
	 * @param url A pointer to a PMML service provider JAR file.
	 *
	 * @see ServiceLoaderUtil#load(Class, ClassLoader)
	 */
	@SuppressWarnings({"cast", "resource"})
	static
	public PMML load(URL url) throws IOException {
		URLClassLoader clazzLoader = URLClassLoader.newInstance(new URL[]{url});

		try {
			return load(clazzLoader);
		} finally {

			if(clazzLoader instanceof Closeable){
				Closeable closeable = (Closeable)clazzLoader;

				closeable.close();
			}
		}
	}

	/**
	 * @param clazzLoader A class loader holding a PMML service provider configuration, classes and resources.
	 *
	 * @see ServiceLoaderUtil#load(Class, ClassLoader)
	 */
	static
	public PMML load(ClassLoader clazzLoader){
		return ServiceLoaderUtil.load(PMML.class, clazzLoader);
	}

	/**
	 * @see JAXBUtil#unmarshalPMML(Source)
	 */
	static
	public PMML unmarshal(InputStream is) throws ParserConfigurationException, SAXException, JAXBException {
		Source source = SAXUtil.createFilteredSource(is, new ImportFilter());

		return JAXBUtil.unmarshalPMML(source);
	}

	/**
	 * @see JAXBUtil#marshalPMML(PMML, Result)
	 */
	static
	public void marshal(PMML pmml, OutputStream os) throws JAXBException {
		StreamResult result = new StreamResult(os);

		JAXBUtil.marshalPMML(pmml, result);
	}
}