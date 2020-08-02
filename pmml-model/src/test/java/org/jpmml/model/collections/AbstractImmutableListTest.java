/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract
public class AbstractImmutableListTest {

	static
	<E> List<E> toList(Iterator<E> it){
		List<E> result = new ArrayList<>();

		while(it.hasNext()){
			result.add(it.next());
		}

		return result;
	}
}