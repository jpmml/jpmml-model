/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.types;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @see Epochs
 */
abstract
public class ComplexPeriod<P extends ComplexPeriod<P>> extends Period<P> {

	private LocalDate epoch = null;


	ComplexPeriod(LocalDate epoch){
		setEpoch(epoch);
	}

	abstract
	public P forEpoch(LocalDate epoch);

	public LocalDate getEpoch(){
		return this.epoch;
	}

	private void setEpoch(LocalDate epoch){
		this.epoch = Objects.requireNonNull(epoch);
	}
}