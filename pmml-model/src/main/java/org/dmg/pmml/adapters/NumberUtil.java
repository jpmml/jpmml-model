/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import jakarta.xml.bind.DatatypeConverter;

public class NumberUtil {

	private NumberUtil(){
	}

	static
	public Integer parseInteger(String value){
		return DatatypeConverter.parseInt(value);
	}

	static
	public String printInteger(Integer value){
		return DatatypeConverter.printInt(value);
	}

	static
	public Number parseNumber(String value){

		try {
			return DatatypeConverter.parseInt(value);
		} catch(NumberFormatException nfe){
			return DatatypeConverter.parseDouble(value);
		}
	}

	static
	public String printNumber(Number value){

		if(value instanceof Float){
			return DatatypeConverter.printFloat(value.floatValue());
		} else

		if(value instanceof Double){
			return DatatypeConverter.printDouble(value.doubleValue());
		} else

		{
			return value.toString();
		}
	}
}