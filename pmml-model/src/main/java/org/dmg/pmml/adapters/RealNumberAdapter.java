/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class RealNumberAdapter extends XmlAdapter<String, Number> {

	@Override
	public Number unmarshal(String value){

		try {
			return DatatypeConverter.parseInt(value);
		} catch(IllegalArgumentException iae){
			return DatatypeConverter.parseDouble(value);
		}
	}

	@Override
	public String marshal(Number value){

		if(value == null){
			return null;
		} // End if

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