/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.types;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.dmg.pmml.DataType;

public class SecondsSinceDate extends ComplexPeriod<SecondsSinceDate> {

	private long seconds = 0;


	private SecondsSinceDate(){
	}

	public SecondsSinceDate(LocalDate epoch, LocalDateTime dateTime){
		this(epoch, ChronoUnit.SECONDS.between(epoch.atStartOfDay(), dateTime));
	}

	public SecondsSinceDate(LocalDate epoch, long seconds){
		super(epoch);

		setSeconds(seconds);
	}

	@Override
	public DataType getDataType(){
		return getDataType(getEpoch());
	}

	@Override
	public SecondsSinceDate forEpoch(LocalDate newEpoch){
		LocalDate epoch = getEpoch();
		long seconds = getSeconds();

		if(Objects.equals(epoch, newEpoch)){
			return this;
		}

		long newSeconds = ChronoUnit.SECONDS.between(newEpoch.atStartOfDay(), epoch.atStartOfDay()) + seconds;

		return new SecondsSinceDate(newEpoch, newSeconds);
	}

	@Override
	public long longValue(){
		return getSeconds();
	}

	@Override
	public int compareTo(SecondsSinceDate that){

		if(!Objects.equals(this.getEpoch(), that.getEpoch())){
			throw new ClassCastException();
		}

		return Long.compare(this.getSeconds(), that.getSeconds());
	}

	@Override
	public int hashCode(){
		return (31 * getEpoch().hashCode()) + Objects.hashCode(getSeconds());
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof SecondsSinceDate){
			SecondsSinceDate that = (SecondsSinceDate)object;

			return Objects.equals(this.getEpoch(), that.getEpoch()) && (this.getSeconds() == that.getSeconds());
		}

		return false;
	}

	public long getSeconds(){
		return this.seconds;
	}

	private void setSeconds(long seconds){
		this.seconds = seconds;
	}

	static
	public DataType getDataType(LocalDate epoch){
		return SecondsSinceDate.dataTypes.get(epoch);
	}

	private static final Map<LocalDate, DataType> dataTypes = new LinkedHashMap<>();

	static {
		dataTypes.put(Epochs.YEAR_1960, DataType.DATE_TIME_SECONDS_SINCE_1960);
		dataTypes.put(Epochs.YEAR_1970, DataType.DATE_TIME_SECONDS_SINCE_1970);
		dataTypes.put(Epochs.YEAR_1980, DataType.DATE_TIME_SECONDS_SINCE_1980);

		dataTypes.put(Epochs.YEAR_1990, DataType.DATE_TIME_SECONDS_SINCE_1990);
		dataTypes.put(Epochs.YEAR_2000, DataType.DATE_TIME_SECONDS_SINCE_2000);
		dataTypes.put(Epochs.YEAR_2010, DataType.DATE_TIME_SECONDS_SINCE_2010);
		dataTypes.put(Epochs.YEAR_2020, DataType.DATE_TIME_SECONDS_SINCE_2020);
	}
}