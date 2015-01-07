/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.dmg.pmml.FieldName;

public class FieldNameAdapter extends XmlAdapter<String, FieldName> {

	@Override
	public FieldName unmarshal(String value){
		return FieldName.create(value);
	}

	@Override
	public String marshal(FieldName value){

		// FieldName corresponds to a simple type in PMML XML Schema. Hence, it is possible to encounter a null instance.
		if(value == null){
			return null;
		}

		return value.getValue();
	}
}