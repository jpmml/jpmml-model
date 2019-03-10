/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.Collection;
import java.util.List;

public class ComplexArray extends Array {

	public ComplexArray(){
	}

	public ComplexArray(Array.Type type, Collection<?> objects){
		super(type, null);

		List<Object> value = getValue();

		if(!objects.isEmpty()){
			value.addAll(objects);
		}
	}

	@Override
	public ComplexArray setN(Integer n){
		return (ComplexArray)super.setN(n);
	}

	@Override
	public ComplexArray setType(Array.Type type){
		return (ComplexArray)super.setType(type);
	}

	@Override
	public List<Object> getValue(){
		Object value = super.getValue();

		if(value == null){
			value = new ComplexArrayList(){

				@Override
				public Array.Type getType(){
					return ComplexArray.this.getType();
				}
			};

			super.setValue(value);
		}

		return (ComplexArrayList)value;
	}

	@Override
	public ComplexArray setValue(Object value){
		throw new UnsupportedOperationException();
	}
}