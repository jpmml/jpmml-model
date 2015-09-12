/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

class Cache<K, V> extends ConcurrentHashMap<K, WeakReference<V>> {

	public void compact(){
		Collection<WeakReference<V>> references = values();

		for(Iterator<WeakReference<V>> it = references.iterator(); it.hasNext(); ){
			WeakReference<V> reference = it.next();

			V value = reference.get();

			if(value == null){
				it.remove();
			}
		}
	}
}