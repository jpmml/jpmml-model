/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.xml.sax.XMLReader;

/**
 * <p>
 * A SAX filter that skips an element.
 * </p>
 */
public class ElementFilter extends SkipFilter {

	private String namespaceURI = null;

	private String localName = null;


	public ElementFilter(String localName){
		this((Version.PMML_4_3).getNamespaceURI(), localName);
	}

	public ElementFilter(String namespaceURI, String localName){
		setNamespaceURI(namespaceURI);
		setLocalName(localName);
	}

	public ElementFilter(Class<? extends PMMLObject> clazz){
		XmlRootElement element = getElement(clazz);

		setNamespaceURI(element.namespace());
		setLocalName(element.name());
	}

	public ElementFilter(XMLReader reader, String localName){
		this(reader, (Version.PMML_4_3).getNamespaceURI(), localName);
	}

	public ElementFilter(XMLReader reader, String namespaceURI, String localName){
		super(reader);

		setNamespaceURI(namespaceURI);
		setLocalName(localName);
	}

	public ElementFilter(XMLReader reader, Class<? extends PMMLObject> clazz){
		super(reader);

		XmlRootElement element = getElement(clazz);

		setNamespaceURI(element.namespace());
		setLocalName(element.name());
	}

	@Override
	public boolean isSkipped(String namespaceURI, String localName){
		return (this.namespaceURI).equals(namespaceURI) && (this.localName).equals(localName);
	}

	public String getNamespaceURI(){
		return this.namespaceURI;
	}

	private void setNamespaceURI(String namespaceURI){

		if(namespaceURI == null){
			throw new NullPointerException();
		}

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
	private XmlRootElement getElement(Class<? extends PMMLObject> clazz){
		XmlRootElement result = clazz.getAnnotation(XmlRootElement.class);

		if(result == null){
			throw new IllegalArgumentException();
		}

		return result;
	}
}