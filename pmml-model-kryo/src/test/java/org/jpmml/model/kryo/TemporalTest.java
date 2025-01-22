/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import org.jpmml.model.temporals.DateTimeUtil;
import org.jpmml.model.temporals.Epochs;
import org.junit.jupiter.api.Test;

public class TemporalTest extends KryoSerializerTest {

	@Test
	public void kryoClone() throws Exception {
		KryoSerializer kryoSerializer = new KryoSerializer(super.kryo);

		checkedCloneRaw(kryoSerializer, DateTimeUtil.parseDate(DATE));
		checkedCloneRaw(kryoSerializer, DateTimeUtil.parseTime(TIME));
		checkedCloneRaw(kryoSerializer, DateTimeUtil.parseDateTime(DATE_TIME));

		checkedCloneRaw(kryoSerializer, DateTimeUtil.parseDaysSinceDate(Epochs.YEAR_1960, DATE));
		checkedCloneRaw(kryoSerializer, DateTimeUtil.parseSecondsSinceMidnight(TIME));
		checkedCloneRaw(kryoSerializer, DateTimeUtil.parseSecondsSinceDate(Epochs.YEAR_1960, DATE_TIME));
	}

	// The date and time (UTC) of the first moon landing
	private static final String DATE = "1969-07-20";
	private static final String TIME = "20:17:40";
	private static final String DATE_TIME = (DATE + "T" + TIME);
}