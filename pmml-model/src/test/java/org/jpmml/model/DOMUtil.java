/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMUtil {

	private DOMUtil(){
	}

	static
	public Node getAttribute(Node node, String name){
		NamedNodeMap attributes = node.getAttributes();

		return attributes.getNamedItem(name);
	}

	static
	public String getAttributeValue(Node node, String name){
		Node attribute = getAttribute(node, name);

		if(attribute != null){
			return attribute.getNodeValue();
		}

		return null;
	}
}