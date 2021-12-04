/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.types;

import java.time.LocalDate;

/**
 * @see Epochs
 */
abstract
class ComplexPeriod<P extends ComplexPeriod<P>> extends Period<P> {

	private LocalDate epoch = null;


	ComplexPeriod(LocalDate epoch){
		setEpoch(epoch);
	}

	public LocalDate getEpoch(){
		return this.epoch;
	}

	private void setEpoch(LocalDate epoch){
		this.epoch = epoch;
	}
}