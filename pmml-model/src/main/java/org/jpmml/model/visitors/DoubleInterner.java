/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dmg.pmml.RealSparseArray;
import org.dmg.pmml.VisitorAction;

/**
 * <p>
 * A Visitor that interns {@link Double} attribute values.
 * </p>
 */
public class DoubleInterner extends NumberInterner<Double> {

	public DoubleInterner(){
		super(Double.class, DoubleInterner.cache);
	}

	@Override
	public Double canonicalize(Double value){
		return value;
	}

	@Override
	public VisitorAction visit(RealSparseArray realSparseArray){

		if(realSparseArray.hasEntries()){
			internAll(realSparseArray.getEntries());
		}

		return super.visit(realSparseArray);
	}

	static
	public void clear(){
		DoubleInterner.cache.clear();
	}

	private static final ConcurrentMap<Double, Double> cache = new ConcurrentHashMap<>();
}