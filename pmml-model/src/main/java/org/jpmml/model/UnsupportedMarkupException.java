/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.PMMLObject;

/**
 * <p>
 * Thrown to indicate that the class model object is not supported.
 * </p>
 */
abstract
public class UnsupportedMarkupException extends MarkupException {

	public UnsupportedMarkupException(String message){
		super(message);
	}

	public UnsupportedMarkupException(String message, PMMLObject context){
		super(message, context);
	}
}