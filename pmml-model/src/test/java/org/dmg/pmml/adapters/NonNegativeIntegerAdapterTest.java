/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NonNegativeIntegerAdapterTest {

	@Test
	public void unmarshal(){
		IntegerAdapter adapter = new NonNegativeIntegerAdapter();

		assertThrows(IllegalArgumentException.class, () -> adapter.unmarshal("-1"));

		assertEquals((Integer)0, adapter.unmarshal("0"));
		assertEquals((Integer)1, adapter.unmarshal("1"));
	}
}