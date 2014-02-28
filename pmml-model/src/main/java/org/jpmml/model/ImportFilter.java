/*
 * Copyright (c) 2009 University of Tartu
 */
package org.jpmml.model;

import javax.xml.transform.sax.*;

import org.dmg.pmml.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * SAX filter for translating PMML schema version 3.X and 4.X documents to PMML schema version 4.2 documents.
 */
public class ImportFilter extends PMMLFilter {

	public ImportFilter(){
		super(Version.PMML_4_2);
	}

	public ImportFilter(XMLReader reader){
		super(reader, Version.PMML_4_2);
	}

	@Override
	public String filterNamespaceURI(String namespaceURI){
		Version version = forNamespaceURI(namespaceURI, getVersion());

		switch(version){
			case PMML_3_0:
			case PMML_3_1:
			case PMML_3_2:
			case PMML_4_0:
			case PMML_4_1:
			case PMML_4_2:
				return getNamespaceURI();
			default:
				break;
		}

		return namespaceURI;
	}

	@Override
	public String filterLocalName(String namespaceURI, String localName){
		Version version = forNamespaceURI(namespaceURI, getVersion());

		if((Version.PMML_4_0).equals(version)){

			if(("Trend").equals(localName)){
				return "Trend_ExpoSmooth";
			}
		}

		return localName;
	}

	/**
	 * @param source An {@link InputSource} that contains PMML schema version 3.X or 4.X document.
	 *
	 * @return A {@link SAXSource} that contains PMML schema version 4.2 document.
	 */
	static
	public SAXSource apply(InputSource source) throws SAXException {
		XMLReader reader = XMLReaderFactory.createXMLReader();

		ImportFilter filter = new ImportFilter(reader);

		return new SAXSource(filter, source);
	}
}