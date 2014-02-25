/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.model;

import java.util.*;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;

import org.dmg.pmml.*;

import org.w3c.dom.Node;

public class ExtensionUtil {

	private ExtensionUtil(){
	}

	static
	public <E extends PMMLObject> E getExtension(Extension extension, Class<? extends E> clazz) throws JAXBException {
		JAXBContext context = JAXBUtil.getContext();

		Unmarshaller unmarshaller = context.createUnmarshaller();

		return getExtension(extension, clazz, unmarshaller);
	}

	static
	public <E extends PMMLObject> E getExtension(Extension extension, Class<? extends E> clazz, Unmarshaller unmarshaller) throws JAXBException {
		XmlRootElement rootElement = clazz.getAnnotation(XmlRootElement.class);
		if(rootElement == null){
			throw new IllegalArgumentException();
		}

		String name = rootElement.name();

		List<?> objects = extension.getContent();
		for(Object object : objects){

			if(object instanceof Node){
				Node node = (Node)object;

				if((name).equals(node.getLocalName())){
					Source source = new DOMSource(node);

					return clazz.cast(JAXBUtil.unmarshal(unmarshaller, source));
				}
			}
		}

		return null;
	}
}