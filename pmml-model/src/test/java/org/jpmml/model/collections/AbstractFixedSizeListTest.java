/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

abstract
public class AbstractFixedSizeListTest {

	static
	<E> List<E> toList(Iterator<E> it){
		List<E> result = new ArrayList<>();

		while(it.hasNext()){
			result.add(it.next());
		}

		return result;
	}

	static
	public <E> void transform(List<E> list, Function<E, E> function){

		for(ListIterator<E> it = list.listIterator(); it.hasNext(); ){
			E element = it.next();

			it.set(function.apply(element));
		}
	}
}