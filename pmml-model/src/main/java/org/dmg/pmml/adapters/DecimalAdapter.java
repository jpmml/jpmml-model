/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import java.math.BigDecimal;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DecimalAdapter extends XmlAdapter<String, Double> {

	@Override
	public Double unmarshal(String value){

		if("0.0".equals(value)){
			return DecimalAdapter.ZERO;
		} else

		if("1.0".equals(value)){
			return DecimalAdapter.ONE;
		}

		BigDecimal decimal = DatatypeConverter.parseDecimal(value);

		if(!ValueUtil.checkRange(decimal, DecimalAdapter.MIN_VALUE, DecimalAdapter.MAX_VALUE)){
			throw new IllegalArgumentException(value);
		}

		return Double.valueOf(decimal.doubleValue());
	}

	@Override
	public String marshal(Double value){

		if(value == null){
			return null;
		}

		return DatatypeConverter.printDecimal(BigDecimal.valueOf(value.doubleValue()));
	}

	public static final Double ZERO = Double.valueOf(0.0d);
	public static final Double ONE = Double.valueOf(1.0d);

	protected static final BigDecimal MIN_VALUE = BigDecimal.valueOf(-Double.MAX_VALUE);
	protected static final BigDecimal MAX_VALUE = BigDecimal.valueOf(Double.MAX_VALUE);
}