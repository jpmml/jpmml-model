/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.temporals;

import java.io.Serializable;
import java.util.IllegalFormatException;

import org.dmg.pmml.ComplexValue;
import org.dmg.pmml.DataType;

abstract
public class Instant<I extends Instant<I>> implements ComplexValue, Comparable<I>, Serializable {

	Instant(){
	}

	abstract
	public DataType getDataType();

	/**
	 * @see String#format(String, Object...)
	 */
	abstract
	public String format(String pattern) throws IllegalFormatException;
}