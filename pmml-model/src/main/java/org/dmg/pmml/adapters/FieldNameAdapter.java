/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class FieldNameAdapter extends XmlAdapter<String, String> {

	@Override
	public String unmarshal(String value){

		if(value == null){
			throw new NullPointerException();
		} else

		if(("").equals(value)){
			throw new IllegalArgumentException("Field name cannot be empty");
		}

		return value.intern();
	}

	@Override
	public String marshal(String value){
		return value;
	}
}