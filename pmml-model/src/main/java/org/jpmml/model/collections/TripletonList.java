/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.collections;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public class TripletonList<E> extends AbstractFixedSizeList<E> implements RandomAccess, Serializable {

	private E first = null;

	private E second = null;

	private E third = null;


	public TripletonList(E first, E second, E third){
		this.first = first;
		this.second = second;
		this.third = third;
	}

	@Override
	public int size(){
		return 3;
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
			case 2:
				return this.third;
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public E set(int index, E element){
		E result;

		switch(index){
			case 0:
				result = this.first;
				this.first = element;
				return result;
			case 1:
				result = this.second;
				this.second = element;
				return result;
			case 2:
				result = this.third;
				this.third = element;
				return result;
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex){

		if(fromIndex < 0 || toIndex > 3){
			throw new IndexOutOfBoundsException();
		}

		int length = (toIndex - fromIndex);
		switch(length){
			case 0:
				return Collections.emptyList();
			case 1:
				return Collections.singletonList(get(fromIndex));
			case 2:
				return new DoubletonList<>(get(fromIndex), get(fromIndex + 1));
			case 3:
				return this;
			default:
				throw new IllegalArgumentException();
		}
	}
}