/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.temporals;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.dmg.pmml.DataType;

public class DaysSinceDate extends ComplexPeriod<DaysSinceDate> {

	private long days = 0;


	private DaysSinceDate(){
	}

	public DaysSinceDate(Date epoch, LocalDate date){
		this(epoch, ChronoUnit.DAYS.between(epoch.getDate(), date));
	}

	public DaysSinceDate(Date epoch, long days){
		super(epoch);

		setDays(days);
	}

	@Override
	public DataType getDataType(){
		return getDataType(getEpoch());
	}

	@Override
	public DaysSinceDate forEpoch(Date newEpoch){
		Date epoch = getEpoch();
		long days = getDays();

		if(Objects.equals(epoch, newEpoch)){
			return this;
		}

		long newDays = ChronoUnit.DAYS.between(newEpoch.getDate(), epoch.getDate()) + days;

		return new DaysSinceDate(newEpoch, newDays);
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

	static
	public DataType getDataType(Date epoch){
		return DaysSinceDate.dataTypes.get(epoch);
	}

	private static final Map<Date, DataType> dataTypes = new LinkedHashMap<>();

	static {
		dataTypes.put(Epochs.YEAR_1960, DataType.DATE_DAYS_SINCE_1960);
		dataTypes.put(Epochs.YEAR_1970, DataType.DATE_DAYS_SINCE_1970);
		dataTypes.put(Epochs.YEAR_1980, DataType.DATE_DAYS_SINCE_1980);

		dataTypes.put(Epochs.YEAR_1990, DataType.DATE_DAYS_SINCE_1990);
		dataTypes.put(Epochs.YEAR_2000, DataType.DATE_DAYS_SINCE_2000);
		dataTypes.put(Epochs.YEAR_2010, DataType.DATE_DAYS_SINCE_2010);
		dataTypes.put(Epochs.YEAR_2020, DataType.DATE_DAYS_SINCE_2020);
	}
}