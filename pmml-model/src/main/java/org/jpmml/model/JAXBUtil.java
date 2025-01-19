/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.xml.sax.SAXException;

public class JAXBUtil {

	private JAXBUtil(){
	}

	static
	public Class<?>[] getObjectFactoryClasses(){
		return new Class<?>[]{
			// org.dmg.pmml.*
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
			// org.jpmml.model.*
			org.jpmml.model.cells.ObjectFactory.class
		};
	}

	static
	public JAXBContext createContext() throws JAXBException {
		return JAXBContext.newInstance(getObjectFactoryClasses());
	}

	static
	public JAXBContext getContext() throws JAXBException {

		if(JAXBUtil.context == null){
			JAXBUtil.context = createContext();
		}

		return JAXBUtil.context;
	}

	static
	public void setContext(JAXBContext context){
		JAXBUtil.context = context;
	}

	static
	public Schema createSchema() throws IOException, SAXException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		URL url = (org.dmg.pmml.ObjectFactory.class).getResource("/pmml.xsd");
		if(url == null){
			throw new FileNotFoundException();
		}

		return schemaFactory.newSchema(url);
	}

	static
	public Schema getSchema() throws IOException, SAXException {

		if(JAXBUtil.schema == null){
			JAXBUtil.schema = createSchema();
		}

		return JAXBUtil.schema;
	}

	static
	public void setSchema(Schema schema){
		JAXBUtil.schema = schema;
	}

	private static JAXBContext context = null;

	private static Schema schema = null;
}