/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

public class ValueUtil {

	private ValueUtil(){
	}

	static
	public <E extends Comparable<E>> boolean checkRange(E value, E min, E max){

		if(min != null && (value).compareTo(min) < 0){
			return false;
		} // End if

		if(max != null && (value).compareTo(max) > 0){
			return false;
		}

		return true;
	}
}