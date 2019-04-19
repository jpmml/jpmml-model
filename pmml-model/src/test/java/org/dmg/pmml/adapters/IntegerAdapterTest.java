/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntegerAdapterTest {

	@Test
	public void unmarshal(){
		IntegerAdapter adapter = new IntegerAdapter();

		assertEquals((Integer)(-1), adapter.unmarshal("-01"));
		assertEquals((Integer)(-1), adapter.unmarshal("-1"));
		assertEquals((Integer)0, adapter.unmarshal("-0"));
		assertEquals((Integer)0, adapter.unmarshal("0"));
		assertEquals((Integer)1, adapter.unmarshal("1"));
		assertEquals((Integer)1, adapter.unmarshal("01"));
	}
}