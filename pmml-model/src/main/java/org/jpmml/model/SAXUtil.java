/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.jpmml.model.filters.CountFilter;
import org.jpmml.model.filters.DepthFilter;
import org.jpmml.model.filters.ExtensionFilter;
import org.jpmml.model.filters.ImportFilter;
import org.jpmml.model.filters.SkipFilter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

public class SAXUtil {

	private SAXUtil(){
	}

	/**
	 * @see ImportFilter
	 * @see SkipFilter
	 * @see ExtensionFilter
	 * @see CountFilter
	 * @see DepthFilter
	 */
	static
	public SAXSource createFilteredSource(InputStream is, XMLFilter... filters) throws ParserConfigurationException, SAXException {
		XMLReader reader = createXMLReader();

		reader = createFilteredReader(reader, filters);

		return new SAXSource(reader, new InputSource(is));
	}

	static
	public XMLReader createXMLReader() throws ParserConfigurationException, SAXException {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);

		parserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		parserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		parserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		parserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		SAXParser parser = parserFactory.newSAXParser();

		return parser.getXMLReader();
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