/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

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

		return value.toString();
	}
}