/*
 * Copyright (c) 2012 University of Tartu
 */
package org.dmg.pmml;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class SparseArray<E extends Number> extends PMMLObject {

	abstract
	public Integer getN();

	abstract
	public void setN(Integer n);

	abstract
	public List<Integer> getIndices();

	abstract
	public List<E> getEntries();
}