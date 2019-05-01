/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jpmml.model.NumberUtil;

public class RealNumberAdapter extends XmlAdapter<String, Number> {

	@Override
	public Number unmarshal(String value){
		return NumberUtil.parseNumber(value);
	}

	@Override
	public String marshal(Number value){

		if(value == null){
			return null;
		}

		return NumberUtil.printNumber(value);
	}
}