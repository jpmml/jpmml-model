/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import java.math.BigDecimal;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DecimalAdapter extends XmlAdapter<String, Double> {

	@Override
	public Double unmarshal(String value){
		BigDecimal decimal = DatatypeConverter.parseDecimal(value);

		if(!ValueUtil.checkRange(decimal, DecimalAdapter.MIN, DecimalAdapter.MAX)){
			throw new IllegalArgumentException(value);
		}

		return Double.valueOf(decimal.doubleValue());
	}

	@Override
	public String marshal(Double value){

		if(value == null){
			return null;
		}

		return DatatypeConverter.printDouble(value.doubleValue());
	}

	private static final BigDecimal MIN = BigDecimal.valueOf(Double.MIN_VALUE);
	private static final BigDecimal MAX = BigDecimal.valueOf(Double.MAX_VALUE);
}