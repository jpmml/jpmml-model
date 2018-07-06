/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;

import javax.xml.transform.sax.SAXSource;

import org.jpmml.model.filters.ElementFilter;
import org.jpmml.model.filters.ExtensionFilter;
import org.jpmml.model.filters.ImportFilter;
import org.jpmml.model.filters.WhitespaceFilter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class SAXUtil {

	private SAXUtil(){
	}

	/**
	 * @see ImportFilter
	 * @see ElementFilter
	 * @see ExtensionFilter
	 * @see WhitespaceFilter
	 */
	static
	public SAXSource createFilteredSource(InputStream is, XMLFilter... filters) throws SAXException {
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

		reader = createFilteredReader(reader, filters);

		return new SAXSource(reader, new InputSource(is));
	}

	static
	public XMLReader createFilteredReader(XMLReader reader, XMLFilter... filters){
		XMLReader result = reader;

		for(XMLFilter filter : filters){
			filter.setParent(result);

			result = filter;
		}

		return result;
	}
}