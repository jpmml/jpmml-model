/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Objects;

import org.dmg.pmml.PMMLObject;
import org.jpmml.model.PMMLObjectCache;

abstract
public class PMMLObjectInterner<E extends PMMLObject> extends ElementInterner<E> {

	private PMMLObjectCache<E> cache = null;


	protected PMMLObjectInterner(Class<? extends E> type, PMMLObjectCache<E> cache){
		super(type);

		setCache(cache);
	}

	@Override
	public E intern(E object){
		PMMLObjectCache<E> cache = getCache();

		return cache.intern(object);
	}

	public PMMLObjectCache<E> getCache(){
		return this.cache;
	}

	private void setCache(PMMLObjectCache<E> cache){
		this.cache = Objects.requireNonNull(cache);
	}
}