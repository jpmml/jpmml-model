/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.DataField;
import org.dmg.pmml.DataType;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.OpType;
import org.dmg.pmml.Visitable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MemoryMeasurerTest {

	@Test
	public void measure(){
		DataField left = new DataField(null, null, null);
		DataField right = new DataField(FieldName.create("x"), OpType.CONTINUOUS, DataType.DOUBLE);

		assertEquals(getSize(left), getSize(right));

		right = right.withName(new FieldName("x"));

		assertNotEquals(getSize(left), getSize(right));
	}

	static
	private long getSize(Visitable visitable){
		MemoryMeasurer measurer = new MemoryMeasurer();
		measurer.applyTo(visitable);

		return measurer.getSize();
	}
}