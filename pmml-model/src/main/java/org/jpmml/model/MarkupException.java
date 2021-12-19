/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.PMMLObject;

abstract
public class MarkupException extends PMMLException {

	public MarkupException(String message){
		super(message);
	}

	public MarkupException(String message, PMMLObject context){
		super(message, context);
	}
}