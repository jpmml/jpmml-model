/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class ObjectAdapter extends XmlAdapter<String, Object> {

	@Override
	public Object unmarshal(String value){
		return value;
	}

	@Override
	public String marshal(Object value){

		if(value == null){
			return null;
		}

		value = ObjectUtil.toSimpleValue(value);

		return value.toString();
	}
}