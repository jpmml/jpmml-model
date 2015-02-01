/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.dmg.pmml.PMML;

public class JAXBUtil {

	private JAXBUtil(){
	}

	/**
	 * Unmarshals a {@link PMML} class model object.
	 *
	 * @param source Input source containing a complete PMML schema version 4.2 document.
	 *
	 * @see ImportFilter
	 */
	static
	public PMML unmarshalPMML(Source source) throws JAXBException {
		return (PMML)unmarshal(source);
	}

	/**
	 * Unmarshals any class model object.
	 *
	 * @param source Input source containing a complete PMML schema version 4.2 document or any fragment of it.
	 */
	static
	public Object unmarshal(Source source) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller();

		return unmarshaller.unmarshal(source);
	}

	/**
	 * Marshals a {@link PMML} class model object.
	 *
	 * @see ExportFilter
	 */
	static
	public void marshalPMML(PMML pmml, Result result) throws JAXBException {
		marshal(pmml, result);
	}

	/**
	 * Marshals any class model object.
	 */
	static
	public void marshal(Object object, Result result) throws JAXBException {
		Marshaller marshaller = createMarshaller();

		marshaller.marshal(object, result);
	}

	static
	public JAXBContext getContext() throws JAXBException {

		if(JAXBUtil.instance == null){
			JAXBUtil.instance = JAXBContext.newInstance(PMML.class);
		}

		return JAXBUtil.instance;
	}

	static
	public Marshaller createMarshaller() throws JAXBException {
		JAXBContext context = getContext();

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		return marshaller;
	}

	static
	public Unmarshaller createUnmarshaller() throws JAXBException {
		JAXBContext context = getContext();

		Unmarshaller unmarshaller = context.createUnmarshaller();

		return unmarshaller;
	}

	private static JAXBContext instance = null;
}