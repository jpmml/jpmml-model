/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.Version;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

abstract
public class PMMLFilter extends XMLFilterImpl {

	private Version version = null;


	public PMMLFilter(Version version){
		setVersion(version);
	}

	public PMMLFilter(XMLReader reader, Version version){
		super(reader);

		setVersion(version);
	}

	abstract
	public String filterNamespaceURI(String namespaceURI);

	abstract
	public String filterLocalName(String namespaceURI, String localName);

	@Override
	public void startPrefixMapping(String prefix, String namespaceURI) throws SAXException {

		if(("").equals(prefix)){
			super.startPrefixMapping("", getNamespaceURI());

			return;
		}

		super.startPrefixMapping(prefix, namespaceURI);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		super.endPrefixMapping(prefix);
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {
		super.startElement(filterNamespaceURI(namespaceURI), filterLocalName(namespaceURI, localName), qualifiedName, attributes);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
		super.endElement(filterNamespaceURI(namespaceURI), filterLocalName(namespaceURI, localName), qualifiedName);
	}

	public String getNamespaceURI(){
		Version version = getVersion();

		return version.getNamespaceURI();
	}

	public Version getVersion(){
		return this.version;
	}

	private void setVersion(Version version){
		this.version = version;
	}

	static
	protected Version forNamespaceURI(String namespaceURI, Version version){

		if(("").equals(namespaceURI)){
			return version;
		}

		return Version.forNamespaceURI(namespaceURI);
	}
}