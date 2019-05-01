/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jpmml.model.NumberUtil;

public class NumberAdapter extends XmlAdapter<String, Number> {

	@Override
	public Number unmarshal(String value){
		Number result = NumberUtil.parseNumber(value);

		if(result instanceof Float){
			Float floatValue = (Float)result;

			if(floatValue.isNaN() || floatValue.isInfinite()){
				throw new IllegalArgumentException(value);
			}

			return floatValue;
		} else

		if(result instanceof Double){
			Double doubleValue = (Double)result;

			if(doubleValue.isNaN() || doubleValue.isInfinite()){
				throw new IllegalArgumentException(value);
			}

			return doubleValue;
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
}