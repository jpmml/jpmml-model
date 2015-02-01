/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.model;

import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.dmg.pmml.Extension;
import org.dmg.pmml.PMMLObject;
import org.w3c.dom.Node;

public class ExtensionUtil {

	private ExtensionUtil(){
	}

	static
	public <E extends PMMLObject> E getExtension(Extension extension, Class<? extends E> clazz) throws JAXBException {
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

					return clazz.cast(JAXBUtil.unmarshal(source));
				}
			}
		}

		return null;
	}
}