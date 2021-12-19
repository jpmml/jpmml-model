/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;

import org.dmg.pmml.PMMLObject;

public class MisplacedAttributeException extends InvalidAttributeException {

	public MisplacedAttributeException(PMMLObject object, Enum<?> value){
		this(object, EnumUtil.getEnumField(object, value), EnumUtil.getEnumValue(value));
	}

	public MisplacedAttributeException(PMMLObject object, Field field, Object value){
		super(formatMessage(XPathUtil.formatAttribute(field, value)), object);
	}

	static
	public String formatMessage(String xPath){
		return "Attribute with value " + xPath + " is not permitted in this location";
	}
}