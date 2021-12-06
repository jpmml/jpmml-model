/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.temporals;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateTimeUtil {

	private DateTimeUtil(){
	}

	static
	public Date parseDate(String value){

		try {
			return Date.parse(value);
		} catch(DateTimeException dte){
			throw new IllegalArgumentException(value, dte);
		}
	}

	static
	public Time parseTime(String value){

		try {
			return Time.parse(value);
		} catch(DateTimeException dte){
			throw new IllegalArgumentException(value, dte);
		}
	}

	static
	public DateTime parseDateTime(String value){

		try {
			return DateTime.parse(value);
		} catch(DateTimeException dte){
			throw new IllegalArgumentException(value, dte);
		}
	}

	static
	public DaysSinceDate parseDaysSinceDate(Date epoch, String value){

		try {
			return new DaysSinceDate(epoch, LocalDate.parse(value));
		} catch(DateTimeException dte){
			throw new IllegalArgumentException(value, dte);
		}
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
	public SecondsSinceDate parseSecondsSinceDate(Date epoch, String value){

		try {
			return new SecondsSinceDate(epoch, LocalDateTime.parse(value));
		} catch(DateTimeException dte){
			throw new IllegalArgumentException(value, dte);
		}
	}
}