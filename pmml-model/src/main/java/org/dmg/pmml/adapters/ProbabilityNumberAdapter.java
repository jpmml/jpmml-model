/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

public class ProbabilityNumberAdapter extends NumberAdapter {

	@Override
	public Number unmarshal(String value){
		Number result = super.unmarshal(value);

		if(!isValid(result)){
			throw new IllegalArgumentException(value);
		}

		return result;
	}

	static
	public boolean isValid(Number value){
		double doubleValue = value.doubleValue();

		return (doubleValue >= 0d && doubleValue <= 1d);
	}
}