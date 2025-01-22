/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class PositiveIntegerAdapterTest {

	@Test
	public void unmarshal(){
		IntegerAdapter adapter = new PositiveIntegerAdapter();

		try {
			adapter.unmarshal("-1");

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}

		try {
			adapter.unmarshal("0");

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}

		assertEquals((Integer)1, adapter.unmarshal("1"));
	}
}