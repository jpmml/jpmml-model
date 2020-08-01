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

		switch(index){
			case 0:
				return this.first;
			case 1:
				return this.second;
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex){

		if(fromIndex < 0 || toIndex > 2){
			throw new IndexOutOfBoundsException();
		}

		int length = (toIndex - fromIndex);
		switch(length){
			case 0:
				return Collections.emptyList();
			case 1:
				return new SingletonList<>(get(fromIndex));
			case 2:
				return this;
			default:
				throw new IllegalArgumentException();
		}
	}
}