/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.temporals;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import org.dmg.pmml.DataType;

public class Time extends Instant<Time> {

	private LocalTime time = null;


	private Time(){
	}

	public Time(int hour, int minute, int second){
		this(LocalTime.of(hour, minute, second));
	}

	public Time(LocalTime time){
		setTime(time);
	}

	@Override
	public DataType getDataType(){
		return DataType.TIME;
	}

	@Override
	public String toSimpleValue(){
		LocalTime time = getTime();

		return time.toString();
	}

	@Override
	public String format(String pattern){
		LocalTime time = getTime();

		return String.format(pattern, time);
	}

	public SecondsSinceMidnight toSecondsSinceMidnight(){
		LocalTime time = getTime();

		return new SecondsSinceMidnight(time.toSecondOfDay());
	}

	@Override
	public int compareTo(Time that){
		return (this.getTime()).compareTo(that.getTime());
	}

	@Override
	public int hashCode(){
		return getTime().hashCode();
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof Time){
			Time that = (Time)object;

			return Objects.equals(this.getTime(), that.getTime());
		}

		return false;
	}

	public LocalTime getTime(){
		return this.time;
	}

	private void setTime(LocalTime time){
		this.time = Objects.requireNonNull(time);
	}

	static
	public Time parse(String value) throws DateTimeParseException {
		return new Time(LocalTime.parse(value));
	}

	static
	public Time valueOf(Object value){

		if(value instanceof LocalTime){
			LocalTime localTime = (LocalTime)value;

			return new Time(localTime);
		} else

		if(value instanceof LocalDateTime){
			LocalDateTime localDateTime = (LocalDateTime)value;

			return new Time(localDateTime.toLocalTime());
		}

		throw new IllegalArgumentException();
	}
}