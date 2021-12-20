/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;

import org.dmg.pmml.PMMLObject;

public class MissingAttributeException extends MissingMarkupException {

	public MissingAttributeException(String message){
		super(message);
	}

	public MissingAttributeException(String message, PMMLObject context){
		super(message, context);
	}

	public MissingAttributeException(PMMLObject object, Field field){
		super(formatMessage(XPathUtil.formatElementOrAttribute(field)), object);
	}

	static
	public String formatMessage(String xPath){
		return "Required attribute " + xPath + " is not defined";
	}
}