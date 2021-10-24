/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class NumberAdapter extends XmlAdapter<String, Number> {

	@Override
	public Number unmarshal(String value){
		Number result = NumberUtil.parseNumber(value);

		if(!isValid(result)){
			throw new IllegalArgumentException(value);
		}

		return result;
	}

	@Override
	public String marshal(Number value){

		if(value == null){
			return null;
		}

		return NumberUtil.printNumber(value);
	}

	static
	public boolean isValid(Number value){

		if(value instanceof Float){
			Float floatValue = (Float)value;

			if(floatValue.isNaN() || floatValue.isInfinite()){
				return false;
			}
		} else

		if(value instanceof Double){
			Double doubleValue = (Double)value;

			if(doubleValue.isNaN() || doubleValue.isInfinite()){
				return false;
			}
		}

		return true;
	}
}