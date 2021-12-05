/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.types;

import java.io.Serializable;

import org.dmg.pmml.ComplexValue;
import org.dmg.pmml.DataType;

abstract
public class Instant<I extends Instant<I>> implements ComplexValue, Comparable<I>, Serializable {

	Instant(){
	}

	abstract
	public DataType getDataType();
}