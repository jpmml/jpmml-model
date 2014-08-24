/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class IntegerAdapterTest {

	@Test
	public void unmarshal(){
		IntegerAdapter adapter = new IntegerAdapter();

		assertEquals((Integer)0, adapter.unmarshal("0"));

		try {
			adapter.unmarshal("0.0");

			fail();
		} catch(NumberFormatException nfe){
			// Ignored
		}
	}

	@Test
	public void marshal(){
		IntegerAdapter adapter = new IntegerAdapter();

		assertEquals("0", adapter.marshal(0));
	}
}