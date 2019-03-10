/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.ArrayList;
import java.util.Collection;

import org.jpmml.model.ArrayUtil;

abstract
public class ComplexArrayList extends ArrayList<Object> implements ComplexValue {

	public ComplexArrayList(){
	}

	public ComplexArrayList(Collection<?> values){
		super(values);
	}

	abstract
	public Array.Type getType();

	@Override
	public Object toSimpleValue(){
		return ArrayUtil.format(getType(), this);
	}
}