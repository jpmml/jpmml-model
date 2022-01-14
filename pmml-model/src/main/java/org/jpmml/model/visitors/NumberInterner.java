/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

abstract
public class NumberInterner<V extends Number> extends AttributeInterner<V> {

	private ConcurrentMap<V, V> cache = null;


	protected NumberInterner(Class<? extends V> type, ConcurrentMap<V, V> cache){
		super(type);

		setCache(cache);
	}

	abstract
	public V canonicalize(V value);

	@Override
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

	public ConcurrentMap<V, V> getCache(){
		return this.cache;
	}

	private void setCache(ConcurrentMap<V, V> cache){
		this.cache = Objects.requireNonNull(cache);
	}
}