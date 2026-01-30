/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.Indexable;
import org.dmg.pmml.PMMLObject;

public class IndexableUtil {

	private IndexableUtil(){
	}

	static
	public <K, E extends PMMLObject & Indexable<K>> Map<K, E> buildMap(List<E> elements){
		Map<K, E> result = new LinkedHashMap<>();

		for(E element : elements){
			K key = element.getKey();

			if(result.containsKey(key)){
				throw new InvalidElementException(element);
			}

			result.put(key, element);
		}

		return result;
	}
}