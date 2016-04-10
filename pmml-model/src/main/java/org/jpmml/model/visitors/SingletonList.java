/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

class SingletonList<E> extends AbstractImmutableList<E> implements RandomAccess, Serializable {

	private E element = null;


	public SingletonList(E element){
		this.element = element;
	}

	@Override
	public int size(){
		return 1;
	}

	@Override
	public boolean isEmpty(){
		return false;
	}

	@Override
	public E get(int index){

		if(index == 0){
			return this.element;
		}

		throw new IndexOutOfBoundsException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex){

		if(fromIndex < 0 || toIndex > 1){
			throw new IndexOutOfBoundsException();
		}

		int length = (toIndex - fromIndex);

		if(length == 0){
			return Collections.emptyList();
		} else

		if(length == 1){
			return this;
		}

		throw new IllegalArgumentException();
	}
}