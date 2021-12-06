/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.temporals;

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
		DaysSinceDate daysSince1960 = DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1960, DATE);
		DaysSinceDate daysSince1970 = DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1970, DATE);
		DaysSinceDate daysSince1980 = DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1980, DATE);

		assertEquals(DataType.DATE_DAYS_SINCE_1960, daysSince1960.getDataType());
		assertEquals(DataType.DATE_DAYS_SINCE_1970, daysSince1970.getDataType());
		assertEquals(DataType.DATE_DAYS_SINCE_1980, daysSince1980.getDataType());

		try {
			int diff = (daysSince1960).compareTo(daysSince1970);

			fail();
		} catch(ClassCastException cce){
			// Ignored
		}

		assertEquals(daysSince1960, daysSince1970.forEpoch(Epochs.YEAR_1960));
		assertEquals(daysSince1960, daysSince1980.forEpoch(Epochs.YEAR_1960));
		assertEquals(daysSince1970, daysSince1960.forEpoch(Epochs.YEAR_1970));
		assertEquals(daysSince1970, daysSince1980.forEpoch(Epochs.YEAR_1970));
		assertEquals(daysSince1980, daysSince1960.forEpoch(Epochs.YEAR_1980));
		assertEquals(daysSince1980, daysSince1970.forEpoch(Epochs.YEAR_1980));

		assertEquals(0L, countDaysSince1960("1960-01-01"));
		assertEquals(1L, countDaysSince1960("1960-01-02"));
		assertEquals(31L, countDaysSince1960("1960-02-01"));

		assertEquals(-1L, countDaysSince1960("1959-12-31"));

		assertEquals(15796L, countDaysSince1960("2003-04-01"));
	}

	@Test
	public void parseSecondsSinceMidnight(){
		SecondsSinceMidnight secondsSinceNoon = DateTimeUtil.parseSecondsSinceMidnight("12:00:00");

		assertEquals(12L * 60 * 60, secondsSinceNoon.longValue());

		assertEquals("43200", String.valueOf(secondsSinceNoon.toSimpleValue()));

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
		SecondsSinceDate secondsSince1960 = DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1960, DATE_TIME);
		SecondsSinceDate secondsSince1970 = DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1970, DATE_TIME);
		SecondsSinceDate secondsSince1980 = DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1980, DATE_TIME);

		assertEquals(DataType.DATE_TIME_SECONDS_SINCE_1960, secondsSince1960.getDataType());
		assertEquals(DataType.DATE_TIME_SECONDS_SINCE_1970, secondsSince1970.getDataType());
		assertEquals(DataType.DATE_TIME_SECONDS_SINCE_1980, secondsSince1980.getDataType());

		try {
			int diff = (secondsSince1960).compareTo(secondsSince1970);

			fail();
		} catch(ClassCastException cce){
			// Ignored
		}

		assertEquals(secondsSince1960, secondsSince1970.forEpoch(Epochs.YEAR_1960));
		assertEquals(secondsSince1960, secondsSince1980.forEpoch(Epochs.YEAR_1960));
		assertEquals(secondsSince1970, secondsSince1960.forEpoch(Epochs.YEAR_1970));
		assertEquals(secondsSince1970, secondsSince1980.forEpoch(Epochs.YEAR_1970));
		assertEquals(secondsSince1980, secondsSince1960.forEpoch(Epochs.YEAR_1980));
		assertEquals(secondsSince1980, secondsSince1970.forEpoch(Epochs.YEAR_1980));

		assertEquals(0L, countSecondsSince1960("1960-01-01T00:00:00"));
		assertEquals(1L, countSecondsSince1960("1960-01-01T00:00:01"));
		assertEquals(60L, countSecondsSince1960("1960-01-01T00:01:00"));

		assertEquals(-1L, countSecondsSince1960("1959-12-31T23:59:59"));

		assertEquals(185403L, countSecondsSince1960("1960-01-03T03:30:03"));
	}

	static
	private long countDaysSince1960(String string){
		DaysSinceDate daysSinceDate = DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1960, string);

		return daysSinceDate.getDays();
	}

	static
	private long countSecondsSinceMidnight(String string){
		SecondsSinceMidnight secondsSinceMidnight = DateTimeUtil.parseSecondsSinceMidnight(string);

		return secondsSinceMidnight.getSeconds();
	}

	static
	private long countSecondsSince1960(String string){
		SecondsSinceDate secondsSinceDate = DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1960, string);

		return secondsSinceDate.getSeconds();
	}

	// The date and time (UTC) of the first moon landing
	private static final String DATE = "1969-07-20";
	private static final String TIME = "20:17:40";
	private static final String DATE_TIME = (DATE + "T" + TIME);
}