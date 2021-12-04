/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.types;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {

	private DateTimeUtil(){
	}

	static
	public LocalDate parseDate(String value){

		try {
			return LocalDate.parse(value);
		} catch(DateTimeException dte){
			throw new IllegalArgumentException(value, dte);
		}
	}

	static
	public LocalTime parseTime(String value){

		try {
			return LocalTime.parse(value);
		} catch(DateTimeException dte){
			throw new IllegalArgumentException(value, dte);
		}
	}

	static
	public LocalDateTime parseDateTime(String value){

		try {
			return LocalDateTime.parse(value);
		} catch(DateTimeException dte){
			throw new IllegalArgumentException(value, dte);
		}
	}

	static
	public DaysSinceDate parseDaysSinceDate(LocalDate epoch, String value){
		return new DaysSinceDate(epoch, parseDate(value));
	}

	static
	public SecondsSinceMidnight parseSecondsSinceMidnight(String value){

		try {
			return SecondsSinceMidnight.parse(value);
		} catch(DateTimeException dte){
			throw new IllegalArgumentException(value, dte);
		}
	}

	static
	public SecondsSinceDate parseSecondsSinceDate(LocalDate epoch, String value){
		return new SecondsSinceDate(epoch, parseDateTime(value));
	}
}