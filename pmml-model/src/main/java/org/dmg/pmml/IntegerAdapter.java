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

		if(!ValueUtil.checkRange(integer, IntegerAdapter.MIN, IntegerAdapter.MAX)){
			throw new IllegalArgumentException(value);
		}

		return Integer.valueOf(integer.intValue());
	}

	@Override
	public String marshal(Integer value){

		if(value == null){
			return null;
		}

		return DatatypeConverter.printInt(value.intValue());
	}

	private static final BigInteger MIN = BigInteger.valueOf(Integer.MIN_VALUE);
	private static final BigInteger MAX = BigInteger.valueOf(Integer.MAX_VALUE);
}