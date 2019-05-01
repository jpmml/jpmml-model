/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * A Visitor that interns {@link Float} attribute values.
 * </p>
 */
public class FloatInterner extends NumberInterner<Float> {

	public FloatInterner(){
		super(Float.class, FloatInterner.CACHE_PROVIDER.get());
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

	public static final ThreadLocal<ConcurrentMap<Float, Float>> CACHE_PROVIDER = new ThreadLocal<ConcurrentMap<Float, Float>>(){

		@Override
		public ConcurrentMap<Float, Float> initialValue(){
			return FloatInterner.cache;
		}
	};
}