/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.List;

import org.dmg.pmml.PMMLObject;

public class MisplacedElementListException extends InvalidElementListException {

	public MisplacedElementListException(List<? extends PMMLObject> objects){
		super(formatMessage(XPathUtil.formatElement((objects.get(0)).getClass())), objects.get(0));
	}

	static
	public String formatMessage(String xPath){
		return "List of elements " + xPath + " is not permitted in this location";
	}
}