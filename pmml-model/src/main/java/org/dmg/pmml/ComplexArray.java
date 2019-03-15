/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jpmml.model.ArrayUtil;

public class ComplexArray extends Array {

	public ComplexArray(){
	}

	public ComplexArray(Array.Type type, Collection<?> value){
		super(type, requireComplexValue(value));
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
	public Collection<?> getValue(){
		return (Collection<?>)super.getValue();
	}

	public ComplexArray setValue(List<?> values){
		return (ComplexArray)super.setValue(new ListValue(values));
	}

	public ComplexArray setValue(Set<?> values){
		return (ComplexArray)super.setValue(new SetValue(values));
	}

	@Override
	public ComplexArray setValue(Object value){
		return (ComplexArray)super.setValue(requireComplexValue(value));
	}

	static
	public <V extends Collection<?> & ComplexValue> V requireComplexValue(Object value){

		if(value == null){
			return null;
		} // End if

		if((value instanceof Collection) && (value instanceof ComplexValue)){
			return (V)value;
		}

		throw new IllegalArgumentException();
	}

	private class ListValue extends ArrayList<Object> implements ComplexValue {

		private ListValue(Collection<?> values){
			super(values);
		}

		@Override
		public Object toSimpleValue(){
			return ArrayUtil.format(getType(), this);
		}
	}

	private class SetValue extends LinkedHashSet<Object> implements ComplexValue {

		private SetValue(Collection<?> values){
			super(values);
		}

		@Override
		public Object toSimpleValue(){
			return ArrayUtil.format(getType(), this);
		}
	}
}