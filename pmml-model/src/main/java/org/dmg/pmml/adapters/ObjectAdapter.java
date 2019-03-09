/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.dmg.pmml.PrimitiveValueWrapper;

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

		if(value instanceof PrimitiveValueWrapper){
			PrimitiveValueWrapper primitiveValueWrapper = (PrimitiveValueWrapper)value;

			value = primitiveValueWrapper.unwrap();
		}

		return value.toString();
	}
}