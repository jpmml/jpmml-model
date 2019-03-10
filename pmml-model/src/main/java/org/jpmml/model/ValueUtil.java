/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.ComplexValue;

public class ValueUtil {

	private ValueUtil(){
	}

	static
	public Object toSimpleValue(Object value){

		if(value instanceof ComplexValue){
			ComplexValue complexValue = (ComplexValue)value;

			return complexValue.toSimpleValue();
		}

		return value;
	}

	static
	public String toString(Object value){
		value = toSimpleValue(value);

		return value.toString();
	}
}