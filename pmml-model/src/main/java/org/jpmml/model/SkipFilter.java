/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.transform.sax.SAXSource;

import org.jpmml.schema.Version;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * <p>
 * A SAX filter that skips a PMML element and its contents.
 * </p>
 */
public class SkipFilter extends XMLFilterImpl {

	private String namespaceURI = null;

	private String localName = null;

	private int depth = 0;


	public SkipFilter(String localName){
		this((Version.PMML_4_2).getNamespaceURI(), localName);
	}

	public SkipFilter(String namespaceURI, String localName){
		setNamespaceURI(namespaceURI);
		setLocalName(localName);
	}

	public SkipFilter(XMLReader reader, String localName){
		this(reader, (Version.PMML_4_2).getNamespaceURI(), localName);
	}

	public SkipFilter(XMLReader reader, String namespaceURI, String localName){
		super(reader);

		setNamespaceURI(namespaceURI);
		setLocalName(localName);
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

		if((this.localName).equals(localName)){

			if(this.namespaceURI == null || (this.namespaceURI).equals(namespaceURI)){
				return true;
			}
		}

		return false;
	}

	private boolean isSkipping(){
		return (this.depth > 0);
	}

	public String getNamespaceURI(){
		return this.namespaceURI;
	}

	private void setNamespaceURI(String namespaceURI){
		this.namespaceURI = namespaceURI;
	}

	public String getLocalName(){
		return this.localName;
	}

	private void setLocalName(String localName){

		if(localName == null){
			throw new NullPointerException();
		}

		this.localName = localName;
	}

	static
	public SAXSource apply(InputSource source, String name) throws SAXException {
		return JAXBUtil.createFilteredSource(source, new SkipFilter(name));
	}
}