/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

public class PositiveIntegerAdapter extends IntegerAdapter {

	@Override
	public Integer unmarshal(String value){
		Integer result = super.unmarshal(value);

		if(!isValid(result)){
			throw new IllegalArgumentException(value);
		}

		return result;
	}

	static
	public boolean isValid(Integer value){
		return (value > 0);
	}
}