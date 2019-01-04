/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FloatInterner extends NumberInterner<Float> {

	public FloatInterner(){
		super(Float.class, FloatInterner.cache);
	}

	@Override
	public Float canonicalize(Float value){
		return value;
	}

	static
	public void clear(){
		FloatInterner.cache.clear();
	}

	private static final ConcurrentMap<Float, Float> cache = new ConcurrentHashMap<>();
}