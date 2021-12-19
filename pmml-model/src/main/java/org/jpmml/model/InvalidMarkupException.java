/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.PMMLObject;

/**
 * <p>
 * Thrown to indicate that the class model object is not valid.
 * </p>
 */
abstract
public class InvalidMarkupException extends MarkupException {

	public InvalidMarkupException(String message){
		super(message);
	}

	public InvalidMarkupException(String message, PMMLObject context){
		super(message, context);
	}
}