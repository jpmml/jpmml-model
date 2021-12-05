/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.types;

import java.time.temporal.ChronoUnit;

import org.dmg.pmml.DataType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DateTimeUtilTest {

	@Test
	public void parseDate(){
		Date date = DateTimeUtil.parseDate(DATE);

		assertEquals(DATE, date.toSimpleValue());
	}

	@Test
	public void parseTime(){
		Time time = DateTimeUtil.parseTime(TIME);

		assertEquals(TIME, time.toSimpleValue());
	}

	@Test
	public void parseDateTime(){
		DateTime dateTime = DateTimeUtil.parseDateTime(DATE_TIME);

		assertEquals(DATE_TIME, dateTime.toSimpleValue());
	}

	@Test
	public void parseDaysSinceDate(){
		DaysSinceDate sixties = DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1960, DATE);
		DaysSinceDate seventies = DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1970, DATE);
		DaysSinceDate eighties = DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1980, DATE);

		assertEquals(DataType.DATE_DAYS_SINCE_1960, sixties.getDataType());
		assertEquals(DataType.DATE_DAYS_SINCE_1970, seventies.getDataType());
		assertEquals(DataType.DATE_DAYS_SINCE_1980, eighties.getDataType());

		assertEquals(10, ChronoUnit.YEARS.between(sixties.getEpoch(), seventies.getEpoch()));
		assertEquals(10, ChronoUnit.YEARS.between(seventies.getEpoch(), eighties.getEpoch()));

		try {
			int diff = (sixties).compareTo(seventies);

			fail();
		} catch(ClassCastException cce){
			// Ignored
		}

		assertEquals(sixties, seventies.forEpoch(Epochs.YEAR_1960));
		assertEquals(sixties, eighties.forEpoch(Epochs.YEAR_1960));
		assertEquals(seventies, sixties.forEpoch(Epochs.YEAR_1970));
		assertEquals(seventies, eighties.forEpoch(Epochs.YEAR_1970));
		assertEquals(eighties, sixties.forEpoch(Epochs.YEAR_1980));
		assertEquals(eighties, seventies.forEpoch(Epochs.YEAR_1980));

		assertEquals(0L, countDaysSince1960("1960-01-01"));
		assertEquals(1L, countDaysSince1960("1960-01-02"));
		assertEquals(31L, countDaysSince1960("1960-02-01"));

		assertEquals(-1L, countDaysSince1960("1959-12-31"));

		assertEquals(15796L, countDaysSince1960("2003-04-01"));
	}

	@Test
	public void parseSecondsSinceMidnight(){
		SecondsSinceMidnight noon = DateTimeUtil.parseSecondsSinceMidnight("12:00:00");

		assertEquals(12L * 60 * 60, noon.longValue());

		assertEquals("43200", String.valueOf(noon.toSimpleValue()));

		assertEquals(0L, countSecondsSinceMidnight("0:00:00"));
		assertEquals(100L, countSecondsSinceMidnight("0:01:40"));
		assertEquals(200L, countSecondsSinceMidnight("0:03:20"));
		assertEquals(1000L, countSecondsSinceMidnight("0:16:40"));
		assertEquals(86400L, countSecondsSinceMidnight("24:00:00"));
		assertEquals(86401L, countSecondsSinceMidnight("24:00:01"));
		assertEquals(100000L, countSecondsSinceMidnight("27:46:40"));

		assertEquals(19410L, countSecondsSinceMidnight("05:23:30"));

		assertEquals(-10L, countSecondsSinceMidnight("-0:00:10"));
		assertEquals(-100L, countSecondsSinceMidnight("-0:01:40"));
		assertEquals(-1000L, countSecondsSinceMidnight("-0:16:40"));
		assertEquals(-10000L, countSecondsSinceMidnight("-2:46:40"));
		assertEquals(-100000L, countSecondsSinceMidnight("-27:46:40"));
	}

	@Test
	public void parseSecondsSinceDate(){
		SecondsSinceDate sixties = DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1960, DATE_TIME);
		SecondsSinceDate seventies = DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1970, DATE_TIME);
		SecondsSinceDate eighties = DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1980, DATE_TIME);

		assertEquals(DataType.DATE_TIME_SECONDS_SINCE_1960, sixties.getDataType());
		assertEquals(DataType.DATE_TIME_SECONDS_SINCE_1970, seventies.getDataType());
		assertEquals(DataType.DATE_TIME_SECONDS_SINCE_1980, eighties.getDataType());

		assertEquals(10, ChronoUnit.YEARS.between(sixties.getEpoch(), seventies.getEpoch()));
		assertEquals(10, ChronoUnit.YEARS.between(seventies.getEpoch(), eighties.getEpoch()));

		try {
			int diff = (sixties).compareTo(seventies);

			fail();
		} catch(ClassCastException cce){
			// Ignored
		}

		assertEquals(sixties, seventies.forEpoch(Epochs.YEAR_1960));
		assertEquals(sixties, eighties.forEpoch(Epochs.YEAR_1960));
		assertEquals(seventies, sixties.forEpoch(Epochs.YEAR_1970));
		assertEquals(seventies, eighties.forEpoch(Epochs.YEAR_1970));
		assertEquals(eighties, sixties.forEpoch(Epochs.YEAR_1980));
		assertEquals(eighties, seventies.forEpoch(Epochs.YEAR_1980));

		assertEquals(0L, countSecondsSince1960("1960-01-01T00:00:00"));
		assertEquals(1L, countSecondsSince1960("1960-01-01T00:00:01"));
		assertEquals(60L, countSecondsSince1960("1960-01-01T00:01:00"));

		assertEquals(-1L, countSecondsSince1960("1959-12-31T23:59:59"));

		assertEquals(185403L, countSecondsSince1960("1960-01-03T03:30:03"));
	}

	static
	private long countDaysSince1960(String string){
		DaysSinceDate period = DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1960, string);

		return period.getDays();
	}

	static
	private long countSecondsSinceMidnight(String string){
		SecondsSinceMidnight period = DateTimeUtil.parseSecondsSinceMidnight(string);

		return period.getSeconds();
	}

	static
	private long countSecondsSince1960(String string){
		SecondsSinceDate period = DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1960, string);

		return period.getSeconds();
	}

	// The date and time (UTC) of the first moon landing
	private static final String DATE = "1969-07-20";
	private static final String TIME = "20:17:40";
	private static final String DATE_TIME = (DATE + "T" + TIME);
}