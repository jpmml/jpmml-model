/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import org.jpmml.schema.Version;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

/**
 * <p>
 * A SAX filter that skips &quot;old-style&quot; PMML extension elements and attributes.
 * </p>
 */
public class ExtensionFilter extends SkipFilter {

	private String namespaceURI = null;


	public ExtensionFilter(){
		this((Version.PMML_4_3).getNamespaceURI());
	}

	public ExtensionFilter(String namespaceURI){
		setNamespaceURI(namespaceURI);
	}

	public ExtensionFilter(XMLReader reader){
		this(reader, (Version.PMML_4_3).getNamespaceURI());
	}

	public ExtensionFilter(XMLReader reader, String namespaceURI){
		super(reader);

		setNamespaceURI(namespaceURI);
	}

	@Override
	public boolean isSkipped(String namespaceURI, String localName){
		return (this.namespaceURI).equals(namespaceURI) && localName.startsWith("X-");
	}

	@Override
	public Attributes filterAttributes(String namespaceURI, String localName, Attributes attributes){
		AttributesImpl result = null;

		for(int i = attributes.getLength() - 1; i >= 0; i--){
			String attributeLocalName = attributes.getLocalName(i);

			if(attributeLocalName.startsWith("x-")){

				if(result == null){
					result = new AttributesImpl(attributes);
				}

				result.removeAttribute(i);
			}
		}

		if(result == null){
			return attributes;
		}

		return result;
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
}