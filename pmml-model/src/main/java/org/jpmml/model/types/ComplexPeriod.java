/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.types;

import java.time.LocalDate;
import java.util.Objects;

import org.dmg.pmml.DataType;

/**
 * @see Epochs
 */
abstract
class ComplexPeriod<P extends ComplexPeriod<P>> extends Period<P> {

	private LocalDate epoch = null;


	ComplexPeriod(LocalDate epoch){
		setEpoch(epoch);
	}

	@Override
	abstract
	public DataType getDataType();

	public LocalDate getEpoch(){
		return this.epoch;
	}

	private void setEpoch(LocalDate epoch){
		this.epoch = Objects.requireNonNull(epoch);
	}
}