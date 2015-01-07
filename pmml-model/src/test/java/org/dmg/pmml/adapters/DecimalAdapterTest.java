/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DecimalAdapterTest {

	@Test
	public void unmarshal(){
		DecimalAdapter adapter = new DecimalAdapter();

		assertEquals((Double)0.0d, adapter.unmarshal("0.0"));
	}

	@Test
	public void marshal(){
		DecimalAdapter adapter = new DecimalAdapter();

		assertEquals("0.0", adapter.marshal(0.0d));
	}
}