/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

abstract
public class AbstractFixedSizeList<E> extends AbstractCollection<E> implements List<E> {

	@Override
	public Iterator<E> iterator(){
		return listIterator();
	}

	@Override
	public ListIterator<E> listIterator(){
		return listIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index){

		if(index < 0 || index > size()){
			throw new IndexOutOfBoundsException();
		}

		ListIterator<E> result = new ListIterator<E>(){

			private int cursor = index;


			@Override
			public int nextIndex(){
				return this.cursor;
			}

			@Override
			public boolean hasNext(){
				return this.cursor != size();
			}

			@Override
			public E next(){

				try {
					int i = this.cursor;

					E next = get(i);

					this.cursor = (i + 1);

					return next;
				} catch(IndexOutOfBoundsException ioobe){
					throw new NoSuchElementException();
				}
			}

			@Override
			public int previousIndex(){
				return this.cursor - 1;
			}

			@Override
			public boolean hasPrevious(){
				return this.cursor != 0;
			}

			@Override
			public E previous(){

				try {
					int i = (this.cursor - 1);

					E previous = get(i);

					this.cursor = i;

					return previous;
				} catch(IndexOutOfBoundsException ioobe){
					throw new NoSuchElementException();
				}
			}

			@Override
			public void add(E element){
				throw new UnsupportedOperationException();
			}

			@Override
			public void set(E element){
				int i = (this.cursor - 1);

				AbstractFixedSizeList.this.set(i, element);
			}

			@Override
			public void remove(){
				throw new UnsupportedOperationException();
			}
		};

		return result;
	}

	@Override
	public void add(int index, E element){
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> elements){
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int index, E element){
		throw new UnsupportedOperationException();
	}

	@Override
	public E remove(int index){
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object object){

		for(ListIterator<E> it = listIterator(); it.hasNext(); ){

			if(Objects.equals(object, it.next())){
				return it.previousIndex();
			}
		}

		return -1;
	}

	@Override
	public int lastIndexOf(Object object){

		for(ListIterator<E> it = listIterator(size()); it.hasPrevious(); ){

			if(Objects.equals(object, it.previous())){
				return it.nextIndex();
			}
		}

		return -1;
	}
}