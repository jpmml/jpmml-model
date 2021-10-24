/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.filters;

import java.util.Objects;

import jakarta.xml.bind.annotation.XmlRootElement;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

abstract
public class ElementFilter extends XMLFilterImpl {

	private String namespaceURI = null;

	private String localName = null;


	/**
	 * @param localName The name of the element.
	 * Use the asterisk symbol (\"*\") to match any name.
	 */
	public ElementFilter(String localName){
		this((Version.PMML_4_4).getNamespaceURI(), localName);
	}

	/**
	 * @param namespaceURI The namespace URI of the element.
	 * Use the asterisk symbol (\"*\") to match any namespace URI.
	 * @param localName The name of the element.
	 * Use the asterisk symbol (\"*\") to match any name.
	 */
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
		this(reader, (Version.PMML_4_4).getNamespaceURI(), localName);
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

	public boolean matches(String namespaceURI, String localName){
		return equals(getNamespaceURI(), namespaceURI) && equals(getLocalName(), localName);
	}

	public String getQualifiedName(){
		String namespaceURI = getNamespaceURI();
		String localName = getLocalName();

		if(!("*").equals(namespaceURI)){

			if(!("*").equals(localName)){
				return namespaceURI + ":" + localName;
			}
		}

		return localName;
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

	static
	private boolean equals(String left, String right){
		return Objects.equals(left, right) || Objects.equals(left, "*");
	}
}