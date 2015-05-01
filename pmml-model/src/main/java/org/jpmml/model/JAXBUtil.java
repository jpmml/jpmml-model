/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.dmg.pmml.ObjectFactory;
import org.dmg.pmml.PMML;
import org.xml.sax.SAXException;

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
	public Schema getSchema() throws IOException, SAXException {

		if(JAXBUtil.schema == null){
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			URL url = ObjectFactory.class.getResource("/pmml.xsd");
			if(url == null){
				throw new FileNotFoundException();
			}

			JAXBUtil.schema = schemaFactory.newSchema(url);
		}

		return JAXBUtil.schema;
	}

	static
	public JAXBContext getContext() throws JAXBException {

		if(JAXBUtil.context == null){
			JAXBUtil.context = JAXBContext.newInstance(ObjectFactory.class);
		}

		return JAXBUtil.context;
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

	private static Schema schema = null;

	private static JAXBContext context = null;
}