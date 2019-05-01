/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dmg.pmml.IntSparseArray;
import org.dmg.pmml.RealSparseArray;
import org.dmg.pmml.VisitorAction;

/**
 * <p>
 * A Visitor that interns {@link Integer} attribute values.
 * </p>
 */
public class IntegerInterner extends NumberInterner<Integer> {

	public IntegerInterner(){
		super(Integer.class, IntegerInterner.CACHE_PROVIDER.get());
	}

	@Override
	public Integer canonicalize(Integer value){
		return Integer.valueOf(value.intValue());
	}

	@Override
	public VisitorAction visit(IntSparseArray intSparseArray){

		if(intSparseArray.hasEntries()){
			internAll(intSparseArray.getEntries());
		} // End if

		if(intSparseArray.hasIndices()){
			internAll(intSparseArray.getIndices());
		}

		return super.visit(intSparseArray);
	}

	@Override
	public VisitorAction visit(RealSparseArray realSparseArray){

		if(realSparseArray.hasIndices()){
			internAll(realSparseArray.getIndices());
		}

		return super.visit(realSparseArray);
	}

	static
	public void clear(){
		IntegerInterner.cache.clear();
	}

	private static final ConcurrentMap<Integer, Integer> cache = new ConcurrentHashMap<>();

	public static final ThreadLocal<ConcurrentMap<Integer, Integer>> CACHE_PROVIDER = new ThreadLocal<ConcurrentMap<Integer, Integer>>(){

		@Override
		public ConcurrentMap<Integer, Integer> initialValue(){
			return IntegerInterner.cache;
		}
	};
}