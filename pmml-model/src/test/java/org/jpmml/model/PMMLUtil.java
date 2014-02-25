/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.*;

import javax.xml.bind.*;
import javax.xml.transform.stream.*;

import org.dmg.pmml.*;

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
	public InputStream getResourceAsStream(Version version){
		String name = version.getNamespaceURI();
		name = name.substring(name.lastIndexOf('/') + 1);

		return PMMLUtil.class.getResourceAsStream("/pmml/" + name.toLowerCase() + ".pmml");
	}
}