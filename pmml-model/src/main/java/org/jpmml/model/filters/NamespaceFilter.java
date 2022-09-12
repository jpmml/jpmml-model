/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.filters;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

abstract
public class NamespaceFilter extends XMLFilterImpl {

	public NamespaceFilter(){
	}

	public NamespaceFilter(XMLReader reader){
		super(reader);
	}

	abstract
	public String filterNamespaceURI(String namespaceURI);

	@Override
	public void startPrefixMapping(String prefix, String namespaceURI) throws SAXException {
		namespaceURI = filterNamespaceURI(namespaceURI);

		super.startPrefixMapping(prefix, namespaceURI);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		super.endPrefixMapping(prefix);
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {
		namespaceURI = filterNamespaceURI(namespaceURI);

		super.startElement(namespaceURI, localName, qualifiedName, attributes);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
		namespaceURI = filterNamespaceURI(namespaceURI);

		super.endElement(namespaceURI, localName, qualifiedName);
	}
}