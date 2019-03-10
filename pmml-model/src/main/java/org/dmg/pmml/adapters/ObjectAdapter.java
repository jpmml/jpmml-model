/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.dmg.pmml.ComplexValue;

public class ObjectAdapter extends XmlAdapter<String, Object> {

	@Override
	public Object unmarshal(String value){
		return value;
	}

	@Override
	public String marshal(Object value){

		if(value == null){
			return null;
		} // End if

		if(value instanceof ComplexValue){
			ComplexValue complexValue = (ComplexValue)value;

			value = complexValue.toSimpleValue();
		}

		return value.toString();
	}
}