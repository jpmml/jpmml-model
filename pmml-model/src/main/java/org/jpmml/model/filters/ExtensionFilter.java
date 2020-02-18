/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.filters;

import org.dmg.pmml.Version;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

/**
 * <p>
 * A SAX filter that skips &quot;old-style&quot; PMML extension elements and attributes.
 * </p>
 */
public class ExtensionFilter extends SkipFilter {

	public ExtensionFilter(){
		this((Version.PMML_4_4).getNamespaceURI());
	}

	public ExtensionFilter(String namespaceURI){
		super(namespaceURI, "*");
	}

	public ExtensionFilter(XMLReader reader){
		this(reader, (Version.PMML_4_4).getNamespaceURI());
	}

	public ExtensionFilter(XMLReader reader, String namespaceURI){
		super(reader, namespaceURI, "*");
	}

	@Override
	public boolean matches(String namespaceURI, String localName){
		return super.matches(namespaceURI, localName) && localName.startsWith("X-");
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
}