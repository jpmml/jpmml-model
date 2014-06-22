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

	static
	public PMML unmarshalPMML(Source source) throws JAXBException {
		JAXBContext context = getContext();

		Unmarshaller unmarshaller = context.createUnmarshaller();

		return unmarshalPMML(unmarshaller, source);
	}

	/**
	 * Unmarshals a {@link PMML} class model object.
	 *
	 * @param source Input source containing a complete PMML schema version 4.2 document.
	 *
	 * @see ImportFilter
	 */
	static
	public PMML unmarshalPMML(Unmarshaller unmarshaller, Source source) throws JAXBException {
		return (PMML)unmarshal(unmarshaller, source);
	}

	/**
	 * Unmarshals any class model object.
	 *
	 * @param source Input source containing a complete PMML schema version 4.2 document or any fragment of it.
	 */
	static
	public Object unmarshal(Unmarshaller unmarshaller, Source source) throws JAXBException {
		return unmarshaller.unmarshal(source);
	}

	static
	public void marshalPMML(PMML pmml, Result result) throws JAXBException {
		JAXBContext context = getContext();

		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		marshalPMML(marshaller, pmml, result);
	}

	/**
	 * Marshals a {@link PMML} class model object.
	 *
	 * @see ExportFilter
	 */
	static
	public void marshalPMML(Marshaller marshaller, PMML pmml, Result result) throws JAXBException {
		marshal(marshaller, pmml, result);
	}

	/**
	 * Marshals any class model object.
	 */
	static
	public void marshal(Marshaller marshaller, Object object, Result result) throws JAXBException {
		marshaller.marshal(object, result);
	}

	static
	public JAXBContext getContext() throws JAXBException {

		if(JAXBUtil.instance == null){
			JAXBUtil.instance = JAXBContext.newInstance(PMML.class);
		}

		return JAXBUtil.instance;
	}

	private static JAXBContext instance = null;
}