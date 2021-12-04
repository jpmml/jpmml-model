/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.types;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DaysSinceDate extends ComplexPeriod<DaysSinceDate> {

	private long days = 0;


	public DaysSinceDate(LocalDate epoch, LocalDate date){
		this(epoch, ChronoUnit.DAYS.between(epoch, date));
	}

	public DaysSinceDate(LocalDate epoch, long days){
		super(epoch);

		setDays(days);
	}

	@Override
	public long longValue(){
		return getDays();
	}

	@Override
	public int compareTo(DaysSinceDate that){

		if(!Objects.equals(this.getEpoch(), that.getEpoch())){
			throw new ClassCastException();
		}

		return Long.compare(this.getDays(), that.getDays());
	}

	@Override
	public int hashCode(){
		return (31 * getEpoch().hashCode()) + Objects.hashCode(getDays());
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof DaysSinceDate){
			DaysSinceDate that = (DaysSinceDate)object;

			return Objects.equals(this.getEpoch(), that.getEpoch()) && (this.getDays() == that.getDays());
		}

		return false;
	}

	public long getDays(){
		return this.days;
	}

	private void setDays(long days){
		this.days = days;
	}
}