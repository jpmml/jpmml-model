/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.filters;

import org.dmg.pmml.PMMLObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * <p>
 * A SAX filter that skips an element.
 * </p>
 */
public class SkipFilter extends ElementFilter {

	private int depth = 0;


	public SkipFilter(String localName){
		super(localName);
	}

	public SkipFilter(String namespaceURI, String localName){
		super(namespaceURI, localName);
	}

	public SkipFilter(Class<? extends PMMLObject> clazz){
		super(clazz);
	}

	public SkipFilter(XMLReader reader, String localName){
		super(reader, localName);
	}

	public SkipFilter(XMLReader reader, String namespaceURI, String localName){
		super(reader, namespaceURI, localName);
	}

	public SkipFilter(XMLReader reader, Class<? extends PMMLObject> clazz){
		super(reader, clazz);
	}

	public Attributes filterAttributes(String namespaceURI, String localName, Attributes attributes){
		return attributes;
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {

		if(matches(namespaceURI, localName)){
			this.depth++;

			return;
		} // End if

		if(isSkipping()){
			return;
		}

		Attributes filteredAttributes = filterAttributes(namespaceURI, localName, attributes);

		super.startElement(namespaceURI, localName, qualifiedName, filteredAttributes);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {

		if(matches(namespaceURI, localName)){
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