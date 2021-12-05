/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import org.jpmml.model.types.DateTimeUtil;
import org.jpmml.model.types.DaysSinceDate;
import org.jpmml.model.types.Epochs;
import org.jpmml.model.types.SecondsSinceDate;
import org.jpmml.model.types.SecondsSinceMidnight;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeTest extends KryoUtilTest {

	@Test
	public void kryoClone() throws Exception {
		DaysSinceDate daysSinceDate = DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1960, DATE);

		assertEquals(daysSinceDate, clone(daysSinceDate));

		SecondsSinceMidnight secondsSinceMidnight = DateTimeUtil.parseSecondsSinceMidnight(TIME);

		assertEquals(secondsSinceMidnight, clone(secondsSinceMidnight));

		SecondsSinceDate secondsSinceDate = DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1960, DATE_TIME);

		assertEquals(secondsSinceDate, clone(secondsSinceDate));
	}

	// The date and time (UTC) of the first moon landing
	private static final String DATE = "1969-07-20";
	private static final String TIME = "20:17:40";
	private static final String DATE_TIME = (DATE + "T" + TIME);
}