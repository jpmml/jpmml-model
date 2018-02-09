/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.dmg.pmml.PMML;
import org.jpmml.model.filters.ImportFilter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class PMMLUtil {

	private PMMLUtil(){
	}

	/**
	 * @see JAXBUtil#unmarshalPMML(Source)
	 */
	static
	public PMML unmarshal(InputStream is) throws SAXException, JAXBException {
		InputSource source = new InputSource(is);

		// Use SAX filtering to transform PMML schema version 3.X and 4.X documents to PMML schema version 4.3 document
		SAXSource transformedSource = ImportFilter.apply(source);

		return JAXBUtil.unmarshalPMML(transformedSource);
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