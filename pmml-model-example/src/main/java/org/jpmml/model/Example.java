/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.dmg.pmml.PMML;
import org.jpmml.model.filters.ImportFilter;
import org.jpmml.model.filters.WhitespaceFilter;
import org.xml.sax.SAXException;

abstract
public class Example {

	static
	public void execute(Class<? extends Example> clazz, String... args) throws Exception {
		Example example = clazz.newInstance();

		JCommander commander = new JCommander(example);
		commander.setProgramName(clazz.getName());

		try {
			commander.parse(args);
		} catch(ParameterException pe){
			commander.usage();

			System.exit(-1);
		}

		example.execute();
	}

	abstract
	public void execute() throws Exception;

	public Unmarshaller createUnmarshaller() throws JAXBException {
		return JAXBUtil.createUnmarshaller();
	}

	public PMML unmarshalPMML(File file) throws JAXBException, SAXException, IOException {
		Unmarshaller unmarshaller = createUnmarshaller();

		try(InputStream is = new FileInputStream(file)){
			Source source = SAXUtil.createFilteredSource(is, new ImportFilter(), new WhitespaceFilter());

			return (PMML)unmarshaller.unmarshal(source);
		}
	}

	public Marshaller createMarshaller() throws JAXBException {
		return JAXBUtil.createMarshaller();
	}

	public void marshalPMML(PMML pmml, File file) throws JAXBException, IOException {
		Marshaller marshaller = createMarshaller();

		try(OutputStream os = new FileOutputStream(file)){
			Result result = new StreamResult(os);

			marshaller.marshal(pmml, result);
		}
	}
}