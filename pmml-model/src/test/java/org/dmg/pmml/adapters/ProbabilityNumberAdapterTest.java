/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ProbabilityNumberAdapterTest {

	@Test
	public void unmarshal(){
		NumberAdapter adapter = new ProbabilityNumberAdapter();

		try {
			adapter.unmarshal("-0.1");

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}

		assertEquals(-0d, adapter.unmarshal("-0.0"));
		assertEquals(0d, adapter.unmarshal("0.0"));
		assertEquals(1d, adapter.unmarshal("1.0"));

		try {
			adapter.unmarshal("1.1");

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}
	}
}