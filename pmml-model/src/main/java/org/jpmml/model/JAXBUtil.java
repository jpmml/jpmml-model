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

import org.dmg.pmml.PMML;
import org.jpmml.model.filters.ExportFilter;
import org.jpmml.model.filters.ImportFilter;
import org.xml.sax.SAXException;

public class JAXBUtil {

	private JAXBUtil(){
	}

	/**
	 * <p>
	 * Unmarshals a {@link PMML} class model object.
	 * </p>
	 *
	 * @param source Input source containing a complete PMML schema version 4.4 document.
	 *
	 * @see ImportFilter
	 */
	static
	public PMML unmarshalPMML(Source source) throws JAXBException {
		return (PMML)unmarshal(source);
	}

	/**
	 * <p>
	 * Unmarshals any class model object.
	 * </p>
	 *
	 * @param source Input source containing a complete PMML schema version 4.4 document or any fragment of it.
	 */
	static
	public Object unmarshal(Source source) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller();

		return unmarshaller.unmarshal(source);
	}

	/**
	 * <p>
	 * Marshals a {@link PMML} class model object.
	 * </p>
	 *
	 * @see ExportFilter
	 */
	static
	public void marshalPMML(PMML pmml, Result result) throws JAXBException {
		marshal(pmml, result);
	}

	/**
	 * <p>
	 * Marshals any class model object.
	 * </p>
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

			URL url = (org.dmg.pmml.ObjectFactory.class).getResource("/pmml.xsd");
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
			JAXBUtil.context = JAXBContext.newInstance(getObjectFactoryClasses());
		}

		return JAXBUtil.context;
	}

	static
	public Class<?>[] getObjectFactoryClasses(){
		return new Class<?>[]{
			org.dmg.pmml.ObjectFactory.class,
			org.dmg.pmml.anomaly_detection.ObjectFactory.class,
			org.dmg.pmml.association.ObjectFactory.class,
			org.dmg.pmml.baseline.ObjectFactory.class,
			org.dmg.pmml.bayesian_network.ObjectFactory.class,
			org.dmg.pmml.clustering.ObjectFactory.class,
			org.dmg.pmml.gaussian_process.ObjectFactory.class,
			org.dmg.pmml.general_regression.ObjectFactory.class,
			org.dmg.pmml.mining.ObjectFactory.class,
			org.dmg.pmml.naive_bayes.ObjectFactory.class,
			org.dmg.pmml.nearest_neighbor.ObjectFactory.class,
			org.dmg.pmml.neural_network.ObjectFactory.class,
			org.dmg.pmml.regression.ObjectFactory.class,
			org.dmg.pmml.rule_set.ObjectFactory.class,
			org.dmg.pmml.scorecard.ObjectFactory.class,
			org.dmg.pmml.sequence.ObjectFactory.class,
			org.dmg.pmml.support_vector_machine.ObjectFactory.class,
			org.dmg.pmml.text.ObjectFactory.class,
			org.dmg.pmml.time_series.ObjectFactory.class,
			org.dmg.pmml.tree.ObjectFactory.class,
			org.jpmml.model.inlinetable.ObjectFactory.class
		};
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