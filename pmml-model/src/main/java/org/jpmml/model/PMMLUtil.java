/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.dmg.pmml.PMML;
import org.jpmml.model.filters.ImportFilter;
import org.jpmml.model.filters.WhitespaceFilter;
import org.xml.sax.SAXException;

public class PMMLUtil {

	private PMMLUtil(){
	}

	/**
	 * @see JAXBUtil#unmarshalPMML(Source)
	 */
	static
	public PMML unmarshal(InputStream is) throws SAXException, JAXBException {
		Source source = SAXUtil.createFilteredSource(is, new ImportFilter(), new WhitespaceFilter());

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