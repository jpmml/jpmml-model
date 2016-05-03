/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.transform.sax.SAXSource;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p>
 * A SAX filter that skips a PMML element and its contents.
 * </p>
 */
public class SkipFilter extends XMLFilterImpl {

	private String name = null;

	private int depth = 0;


	public SkipFilter(String name){
		setName(name);
	}

	public SkipFilter(XMLReader reader, String name){
		super(reader);

		setName(name);
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {

		if(isSkipped(namespaceURI, localName)){
			this.depth++;

			return;
		} // End if

		if(isSkipping()){
			return;
		}

		super.startElement(namespaceURI, localName, qualifiedName, attributes);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {

		if(isSkipped(namespaceURI, localName)){
			this.depth--;

			return;
		} // End if

		if(isSkipping()){
			return;
		}

		super.endElement(namespaceURI, localName, qualifiedName);
	}

	@Override
	public void characters(char[] buffer, int index, int length) throws SAXException {

		if(isSkipping()){
			return;
		}

		super.characters(buffer, index, length);
	}

	private boolean isSkipped(String namespaceURI, String localName){
		String name = getName();

		return (name != null) && (name).equals(localName);
	}

	private boolean isSkipping(){
		return (this.depth > 0);
	}

	public String getName(){
		return this.name;
	}

	private void setName(String name){
		this.name = name;
	}

	static
	public SAXSource apply(InputSource source, String name) throws SAXException {
		XMLReader reader = XMLReaderFactory.createXMLReader();

		SkipFilter filter = new SkipFilter(reader, name);

		return new SAXSource(filter, source);
	}
}