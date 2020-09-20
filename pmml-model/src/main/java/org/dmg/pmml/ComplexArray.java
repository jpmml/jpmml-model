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
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

public class ComplexArray extends Array {

	public ComplexArray(){
	}

	@ValueConstructor
	public ComplexArray(@Property("type") Array.Type type, @Property("value") Collection<?> value){
		super(type, requireComplexValue(value));
	}

	@Override
	public ComplexArray setN(@Property("n") Integer n){
		return (ComplexArray)super.setN(n);
	}

	@Override
	public ComplexArray setType(@Property("type") Array.Type type){
		return (ComplexArray)super.setType(type);
	}

	@Override
	public Collection<?> getValue(){
		return (Collection<?>)super.getValue();
	}

	public ComplexArray setValue(List<?> values){
		return (ComplexArray)super.setValue(new ListValue(this, values));
	}

	public ComplexArray setValue(Set<?> values){
		return (ComplexArray)super.setValue(new SetValue(this, values));
	}

	@Override
	public ComplexArray setValue(@Property("value") Object value){
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

	static
	public class ListValue extends ArrayList<Object> implements ComplexValue {

		private Array array = null;


		private ListValue(){
		}

		public ListValue(ComplexArray complexArray, Collection<?> values){
			super(values);

			setArray(complexArray);
		}

		@Override
		public Object toSimpleValue(){
			Array array = getArray();

			return ArrayUtil.format(array.getType(), this);
		}

		public Array getArray(){
			return this.array;
		}

		private void setArray(Array array){
			this.array = array;
		}
	}

	static
	public class SetValue extends LinkedHashSet<Object> implements ComplexValue {

		private Array array = null;


		private SetValue(){
		}

		public SetValue(ComplexArray complexArray, Collection<?> values){
			super(values);

			setArray(complexArray);
		}

		@Override
		public Object toSimpleValue(){
			Array array = getArray();

			return ArrayUtil.format(array.getType(), this);
		}

		public Array getArray(){
			return this.array;
		}

		private void setArray(Array array){
			this.array = array;
		}
	}
}