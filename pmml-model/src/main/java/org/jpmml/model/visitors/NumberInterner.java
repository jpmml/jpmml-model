/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentMap;

abstract
public class NumberInterner<V extends Number> extends AbstractSimpleVisitor {

	private ConcurrentMap<V, V> cache = null;


	protected NumberInterner(ConcurrentMap<V, V> cache){
		setCache(cache);
	}

	abstract
	public V canonicalize(V value);

	public V intern(V value){
		ConcurrentMap<V, V> cache = getCache();

		if(value == null){
			return null;
		}

		V canonicalValue = cache.get(value);
		if(canonicalValue == null){
			canonicalValue = canonicalize(value);

			cache.putIfAbsent(value, canonicalValue);
		}

		return canonicalValue;
	}

	public void internAll(List<V> values){

		for(ListIterator<V> it = values.listIterator(); it.hasNext(); ){
			it.set(intern(it.next()));
		}
	}

	public ConcurrentMap<V, V> getCache(){
		return this.cache;
	}

	private void setCache(ConcurrentMap<V, V> cache){
		this.cache = cache;
	}
}