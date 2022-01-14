/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.concurrent.ConcurrentHashMap;

import org.dmg.pmml.PMMLObject;

public class PMMLObjectCache<E extends PMMLObject> extends ConcurrentHashMap<PMMLObjectKey, E> {

	public E intern(E object){
		PMMLObjectKey key = new PMMLObjectKey(object);

		E internedObject = putIfAbsent(key, object);
		if(internedObject == null){
			return object;
		}

		return internedObject;
	}
}