/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;

import org.dmg.pmml.PMMLObject;

public class MissingElementException extends MissingMarkupException {

	public MissingElementException(String message){
		super(message);
	}

	public MissingElementException(String message, PMMLObject context){
		super(message, context);
	}

	public MissingElementException(PMMLObject object, Field field){
		super(formatMessage(XPathUtil.formatElementOrAttribute(field)), object);
	}

	static
	public String formatMessage(String xPath){
		return "Required element " + xPath + " is not defined";
	}
}