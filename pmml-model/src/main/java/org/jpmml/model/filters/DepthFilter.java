/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.filters;

import org.dmg.pmml.PMMLObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * <p>
 * A SAX filter that limits the nesting depth of elements.
 * </p>
 */
public class DepthFilter extends ElementFilter {

	private int depth = 0;

	private int limit = 0;


	public DepthFilter(String localName, int limit){
		super(localName);

		setLimit(limit);
	}

	public DepthFilter(String namespaceURI, String localName, int limit){
		super(namespaceURI, localName);

		setLimit(limit);
	}

	public DepthFilter(Class<? extends PMMLObject> clazz, int limit){
		super(clazz);

		setLimit(limit);
	}

	public DepthFilter(XMLReader reader, String localName, int limit){
		super(reader, localName);

		setLimit(limit);
	}

	public DepthFilter(XMLReader reader, String namespaceURI, String localName, int limit){
		super(reader, namespaceURI, localName);

		setLimit(limit);
	}

	public DepthFilter(XMLReader reader, Class<? extends PMMLObject> clazz, int limit){
		super(reader, clazz);

		setLimit(limit);
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {

		if(matches(namespaceURI, localName)){
			int limit = getLimit();

			this.depth++;

			if(this.depth > limit){
				throw new SAXException("Too deeply nested " + getQualifiedName() + " elements");
			}
		}

		super.startElement(namespaceURI, localName, qualifiedName, attributes);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {

		if(matches(namespaceURI, localName)){
			this.depth--;
		}

		super.endElement(namespaceURI, localName, qualifiedName);
	}

	public int getLimit(){
		return this.limit;
	}

	private void setLimit(int limit){

		if(limit < 0){
			throw new IllegalArgumentException();
		}

		this.limit = limit;
	}
}