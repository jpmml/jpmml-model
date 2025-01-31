/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProbabilityNumberAdapterTest {

	@Test
	public void unmarshal(){
		NumberAdapter adapter = new ProbabilityNumberAdapter();

		assertThrows(IllegalArgumentException.class, () -> adapter.unmarshal("-0.1"));

		assertEquals(-0d, adapter.unmarshal("-0.0"));
		assertEquals(0d, adapter.unmarshal("0.0"));
		assertEquals(1d, adapter.unmarshal("1.0"));

		assertThrows(IllegalArgumentException.class, () -> adapter.unmarshal("1.1"));
	}
}