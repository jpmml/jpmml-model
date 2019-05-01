/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ProbabilityNumberAdapterTest {

	@Test
	public void unmarshal(){
		RealNumberAdapter adapter = new ProbabilityNumberAdapter();

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