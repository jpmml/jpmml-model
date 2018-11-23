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
 * A SAX filter that limits the number of elements.
 * </p>
 */
public class CountFilter extends ElementFilter {

	private int count = 0;

	private int limit = 0;


	public CountFilter(String localName, int limit){
		super(localName);

		setLimit(limit);
	}

	public CountFilter(String namespaceURI, String localName, int limit){
		super(namespaceURI, localName);

		setLimit(limit);
	}

	public CountFilter(Class<? extends PMMLObject> clazz, int limit){
		super(clazz);

		setLimit(limit);
	}

	public CountFilter(XMLReader reader, String localName, int limit){
		super(reader, localName);

		setLimit(limit);
	}

	public CountFilter(XMLReader reader, String namespaceURI, String localName, int limit){
		super(reader, namespaceURI, localName);

		setLimit(limit);
	}

	public CountFilter(XMLReader reader, Class<? extends PMMLObject> clazz, int limit){
		super(reader, clazz);

		setLimit(limit);
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {

		if(matches(namespaceURI, localName)){
			int limit = getLimit();

			this.count++;

			if(this.count > limit){
				throw new SAXException("Too many " + getQualifiedName() + " elements");
			}
		}

		super.startElement(namespaceURI, localName, qualifiedName, attributes);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
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