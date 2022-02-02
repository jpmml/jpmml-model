/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;

import org.dmg.pmml.PMMLObject;

public class UnsupportedAttributeException extends UnsupportedMarkupException {

	public UnsupportedAttributeException(String message){
		super(message);
	}

	public UnsupportedAttributeException(String message, PMMLObject context){
		super(message, context);
	}

	public UnsupportedAttributeException(PMMLObject object, Enum<?> value){
		this(object, EnumUtil.getEnumField(object, value), EnumUtil.getEnumValue(value));
	}

	public UnsupportedAttributeException(PMMLObject object, Field field, Object value){
		super("Attribute with value " + XPathUtil.formatAttribute(field, value) + " is not supported", object);
	}
}