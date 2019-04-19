/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

public class ProbabilityNumberAdapter extends RealNumberAdapter {

	@Override
	public Number unmarshal(String value){
		Number result = super.unmarshal(value);

		if(result.doubleValue() < 0d || result.doubleValue() > 1d){
			throw new IllegalArgumentException(value);
		}

		return result;
	}
}