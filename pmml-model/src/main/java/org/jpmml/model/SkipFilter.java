/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

abstract
public class SkipFilter extends XMLFilterImpl {

	private int depth = 0;


	public SkipFilter(){
	}

	public SkipFilter(XMLReader reader){
		super(reader);
	}

	abstract
	public boolean isSkipped(String namespaceURI, String localName);

	@Override
	public void startDocument() throws SAXException {

		if(isSkipping()){
			throw new SAXException();
		}

		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {

		if(isSkipping()){
			throw new SAXException();
		}

		super.endDocument();
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

	private boolean isSkipping(){
		return (this.depth > 0);
	}
}