/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class SparseArray<E extends Number> extends PMMLObject {

	abstract
	public Integer getN();

	abstract
	public SparseArray<E> setN(Integer n);

	abstract
	public E getDefaultValue();

	abstract
	public SparseArray<E> setDefaultValue(E defaultValue);

	abstract
	public boolean hasIndices();

	abstract
	public List<Integer> getIndices();

	abstract
	public SparseArray<E> addIndices(Integer... indices);

	abstract
	public boolean hasEntries();

	abstract
	public List<E> getEntries();

	abstract
	public SparseArray<E> addEntries(E... entries);
}