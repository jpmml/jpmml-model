/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.temporals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import org.dmg.pmml.DataType;

public class Date extends Instant<Date> {

	private LocalDate date = null;


	private Date(){
	}

	public Date(int year, int month, int dayOfMonth){
		this(LocalDate.of(year, month, dayOfMonth));
	}

	public Date(LocalDate date){
		setDate(date);
	}

	@Override
	public DataType getDataType(){
		return DataType.DATE;
	}

	@Override
	public String toSimpleValue(){
		LocalDate date = getDate();

		return date.toString();
	}

	@Override
	public String format(String pattern){
		LocalDate date = getDate();

		return String.format(pattern, date);
	}

	public DaysSinceDate toDaysSinceYear(int year){
		LocalDate date = getDate();

		return new DaysSinceDate(new Date(year, 1, 1), date);
	}

	@Override
	public int compareTo(Date that){
		return (this.getDate()).compareTo(that.getDate());
	}

	@Override
	public int hashCode(){
		return getDate().hashCode();
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof Date){
			Date that = (Date)object;

			return Objects.equals(this.getDate(), that.getDate());
		}

		return false;
	}

	public LocalDate getDate(){
		return this.date;
	}

	private void setDate(LocalDate date){
		this.date = Objects.requireNonNull(date);
	}

	static
	public Date parse(String value) throws DateTimeParseException {
		return new Date(LocalDate.parse(value));
	}

	static
	public Date valueOf(Object value){

		if(value instanceof LocalDate){
			LocalDate localDate = (LocalDate)value;

			return new Date(localDate);
		} else

		if(value instanceof LocalDateTime){
			LocalDateTime localDateTime = (LocalDateTime)value;

			return new Date(localDateTime.toLocalDate());
		}

		throw new IllegalArgumentException();
	}
}