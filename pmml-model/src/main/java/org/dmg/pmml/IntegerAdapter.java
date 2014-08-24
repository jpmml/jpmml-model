/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import java.math.BigInteger;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerAdapter extends XmlAdapter<String, Integer> {

	@Override
	public Integer unmarshal(String value){
		BigInteger integer = DatatypeConverter.parseInteger(value);

		if(!ValueUtil.checkRange(integer, IntegerAdapter.MIN_VALUE, IntegerAdapter.MAX_VALUE)){
			throw new IllegalArgumentException(value);
		}

		return Integer.valueOf(integer.intValue());
	}

	@Override
	public String marshal(Integer value){

		if(value == null){
			return null;
		}

		return DatatypeConverter.printInteger(BigInteger.valueOf(value.intValue()));
	}

	protected static final BigInteger MIN_VALUE = BigInteger.valueOf(Integer.MIN_VALUE);
	protected static final BigInteger MAX_VALUE = BigInteger.valueOf(Integer.MAX_VALUE);
}