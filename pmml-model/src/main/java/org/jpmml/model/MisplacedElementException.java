/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.PMMLObject;

public class MisplacedElementException extends InvalidElementException {

	public MisplacedElementException(PMMLObject object){
		super(formatMessage(XPathUtil.formatElement(object.getClass())), object);
	}

	static
	public String formatMessage(String xPath){
		return "Element " + xPath + " is not permitted in this location";
	}
}