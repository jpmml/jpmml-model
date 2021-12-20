/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.PMMLObject;

abstract
public class MissingMarkupException extends MarkupException {

	public MissingMarkupException(String message){
		super(message);
	}

	public MissingMarkupException(String message, PMMLObject context){
		super(message, context);
	}
}