/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.adapters.*;

public class FieldNameAdapter extends XmlAdapter<String, FieldName> {

	@Override
	public FieldName unmarshal(String value){
		return FieldName.unmarshal(value);
	}

	@Override
	public String marshal(FieldName value){

		if(value == null){
			return null;
		}

		return FieldName.marshal(value);
	}
}