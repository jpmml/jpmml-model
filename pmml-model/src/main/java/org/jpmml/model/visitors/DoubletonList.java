/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

class DoubletonList<E> extends AbstractImmutableList<E> implements RandomAccess, Serializable {

	private E first = null;

	private E second = null;


	public DoubletonList(E first, E second){
		this.first = first;
		this.second = second;
	}

	@Override
	public int size(){
		return 2;
	}

	@Override
	public boolean isEmpty(){
		return false;
	}

	@Override
	public E get(int index){

		if(index == 0){
			return this.first;
		} else

		if(index == 1){
			return this.second;
		}

		throw new IndexOutOfBoundsException();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex){

		if(fromIndex < 0 || toIndex > 2){
			throw new IndexOutOfBoundsException();
		}

		int length = (toIndex - fromIndex);

		if(length == 0){
			return Collections.emptyList();
		} else

		if(length == 1){
			return new SingletonList(get(fromIndex));
		} else

		if(length == 2){
			return this;
		}

		throw new IllegalArgumentException();
	}
}