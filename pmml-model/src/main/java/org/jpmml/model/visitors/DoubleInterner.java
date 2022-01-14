/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * A Visitor that interns {@link Double} attribute values.
 * </p>
 */
public class DoubleInterner extends NumberInterner<Double> {

	public DoubleInterner(){
		super(Double.class, DoubleInterner.CACHE_PROVIDER.get());
	}

	@Override
	public Double canonicalize(Double value){
		return value;
	}

	static
	public void clear(){
		DoubleInterner.cache.clear();
	}

	private static final ConcurrentMap<Double, Double> cache = new ConcurrentHashMap<>();

	public static final ThreadLocal<ConcurrentMap<Double, Double>> CACHE_PROVIDER = new ThreadLocal<ConcurrentMap<Double, Double>>(){

		@Override
		public ConcurrentMap<Double, Double> initialValue(){
			return DoubleInterner.cache;
		}
	};
}