/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.transform.sax.*;

import org.dmg.pmml.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * SAX filter for translating PMML schema version 4.2 documents to PMML schema version 3.X and 4.X documents.
 */
public class ExportFilter extends PMMLFilter {

	public ExportFilter(Version version){
		super(version);
	}

	public ExportFilter(XMLReader reader, Version version){
		super(reader, version);
	}

	@Override
	public String filterNamespaceURI(String namespaceURI){

		if(("").equals(namespaceURI)){
			return "";
		}

		return getNamespaceURI();
	}

	@Override
	public String filterLocalName(String namespaceURI, String localName){
		Version version = getVersion();

		if("Trend_ExpoSmooth".equals(localName)){

			if((Version.PMML_4_0).equals(version)){
				return "Trend";
			}
		}

		return localName;
	}

	/**
	 * @param source An {@link InputSource} that contains PMML schema version 4.2 document.
	 * @param version The target PMML schema version.
	 *
	 * @return A {@link SAXSource} containing the target PMML schema version document.
	 */
	static
	public SAXSource apply(InputSource source, Version version) throws SAXException {
		XMLReader reader = XMLReaderFactory.createXMLReader();

		ExportFilter filter = new ExportFilter(reader, version);

		return new SAXSource(filter, source);
	}
}