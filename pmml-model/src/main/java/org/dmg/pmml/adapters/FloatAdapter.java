/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class FloatAdapter extends XmlAdapter<String, Double> {

	@Override
	public Double unmarshal(String value){
		return Double.valueOf(DatatypeConverter.parseFloat(value));
	}

	@Override
	public String marshal(Double value){

		if(value == null){
			return null;
		}

		return DatatypeConverter.printFloat(value.floatValue());
	}
}