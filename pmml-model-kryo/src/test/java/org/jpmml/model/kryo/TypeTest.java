/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import org.jpmml.model.types.DateTimeUtil;
import org.jpmml.model.types.Epochs;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class TypeTest extends KryoUtilTest {

	@Test
	public void kryoClone() throws Exception {
		check(DateTimeUtil.parseDate(DATE));
		check(DateTimeUtil.parseTime(TIME));
		check(DateTimeUtil.parseDateTime(DATE_TIME));

		check(DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1960, DATE));
		check(DateTimeUtil.parseSecondsSinceMidnight(TIME));
		check(DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1960, DATE_TIME));
	}

	private void check(Object object){
		Object clonedObject = clone(object);

		assertEquals(object, clonedObject);
		assertNotSame(object, clonedObject);
	}

	// The date and time (UTC) of the first moon landing
	private static final String DATE = "1969-07-20";
	private static final String TIME = "20:17:40";
	private static final String DATE_TIME = (DATE + "T" + TIME);
}