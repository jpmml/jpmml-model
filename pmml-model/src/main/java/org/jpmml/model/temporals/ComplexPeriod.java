/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.temporals;

import java.util.Objects;

/**
 * @see Epochs
 */
abstract
public class ComplexPeriod<P extends ComplexPeriod<P>> extends Period<P> {

	private Date epoch = null;


	public ComplexPeriod(){
	}

	ComplexPeriod(Date epoch){
		setEpoch(epoch);
	}

	abstract
	public P forEpoch(Date epoch);

	public Date getEpoch(){
		return this.epoch;
	}

	private void setEpoch(Date epoch){
		this.epoch = Objects.requireNonNull(epoch);
	}
}