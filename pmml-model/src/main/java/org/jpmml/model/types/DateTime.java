/*
 * Copyright (c) Villu Ruusmann
 */
package org.jpmml.model.types;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import org.dmg.pmml.DataType;

public class DateTime extends Instant<DateTime> {

	private LocalDateTime dateTime = null;


	private DateTime(){
	}

	public DateTime(int year, int month, int dayOfMonth, int hour, int minute, int second){
		this(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second));
	}

	public DateTime(LocalDateTime dateTime){
		setDateTime(dateTime);
	}

	@Override
	public DataType getDataType(){
		return DataType.DATE_TIME;
	}

	@Override
	public String toSimpleValue(){
		LocalDateTime dateTime = getDateTime();

		return dateTime.toString();
	}

	@Override
	public int compareTo(DateTime that){
		return (this.getDateTime()).compareTo(that.getDateTime());
	}

	@Override
	public int hashCode(){
		return getDateTime().hashCode();
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof DateTime){
			DateTime that = (DateTime)object;

			return Objects.equals(this.getDateTime(), that.getDateTime());
		}

		return false;
	}

	public LocalDateTime getDateTime(){
		return this.dateTime;
	}

	private void setDateTime(LocalDateTime dateTime){
		this.dateTime = Objects.requireNonNull(dateTime);
	}

	static
	public DateTime parse(String value) throws DateTimeParseException {
		return new DateTime(LocalDateTime.parse(value));
	}
}