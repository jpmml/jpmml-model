/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

abstract
public class Example {

	abstract
	public void execute() throws Exception;

	protected Marshaller createMarshaller() throws JAXBException {
		return JAXBUtil.createMarshaller();
	}

	protected Unmarshaller createUnmarshaller() throws JAXBException {
		return JAXBUtil.createUnmarshaller();
	}

	static
	public <E extends Example> void execute(Class<? extends E> clazz, String... args) throws Exception {
		E example = clazz.newInstance();

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
}