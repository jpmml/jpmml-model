/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerAdapter extends XmlAdapter<String, Integer> {

	@Override
	public Integer unmarshal(String value){
		return NumberUtil.parseInteger(value);
	}

	@Override
	public String marshal(Integer value){

		if(value == null){
			return null;
		}

		return NumberUtil.printInteger(value);
	}
}