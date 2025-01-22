/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.DataField;
import org.dmg.pmml.DataType;
import org.dmg.pmml.Interval;
import org.dmg.pmml.OpType;
import org.dmg.pmml.Visitable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemoryMeasurerTest {

	@Test
	public void measure(){
		Interval interval = new Interval(Interval.Closure.CLOSED_CLOSED)
			.setLeftMargin(0d)
			.setRightMargin(1d);

		DataField left = new DataField("x", null, null)
			.addIntervals(interval);

		DataField right = new DataField("x", OpType.CONTINUOUS, DataType.DOUBLE)
			.addIntervals(interval);

		assertEquals(getSize(left), getSize(right));
	}

	static
	private long getSize(Visitable visitable){
		MemoryMeasurer measurer = new MemoryMeasurer();
		measurer.applyTo(visitable);

		return measurer.getSize();
	}
}