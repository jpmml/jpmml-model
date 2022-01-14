/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.PMMLObjectKey;

abstract
public class PMMLObjectInterner<E extends PMMLObject> extends ElementInterner<E> {

	private ConcurrentMap<PMMLObjectKey, E> cache = null;


	protected PMMLObjectInterner(Class<? extends E> type, ConcurrentMap<PMMLObjectKey, E> cache){
		super(type);

		setCache(cache);
	}

	@Override
	public E intern(E object){
		ConcurrentMap<PMMLObjectKey, E> cache = getCache();

		PMMLObjectKey key = new PMMLObjectKey(object);

		E internedObject = cache.putIfAbsent(key, object);
		if(internedObject == null){
			internedObject = object;
		}

		return internedObject;
	}

	public ConcurrentMap<PMMLObjectKey, E> getCache(){
		return this.cache;
	}

	private void setCache(ConcurrentMap<PMMLObjectKey, E> cache){
		this.cache = Objects.requireNonNull(cache);
	}
}