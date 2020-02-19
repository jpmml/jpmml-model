/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.dmg.pmml.ComplexValue;
import org.dmg.pmml.Entity;

public class ObjectUtil {

	private ObjectUtil(){
	}

	static
	public Object toSimpleValue(Object value){

		if(value instanceof Entity){
			Entity<?> entity = (Entity<?>)value;

			return entity.getId();
		} // End if

		if(value instanceof ComplexValue){
			ComplexValue complexValue = (ComplexValue)value;

			return complexValue.toSimpleValue();
		}

		return value;
	}
}