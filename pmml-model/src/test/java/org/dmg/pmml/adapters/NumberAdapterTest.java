/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberAdapterTest {

	@Test
	public void unmarshal(){
		NumberAdapter adapter = new NumberAdapter();

		assertThrows(IllegalArgumentException.class, () -> adapter.unmarshal("NaN"));
		assertThrows(IllegalArgumentException.class, () -> adapter.unmarshal("INF"));

		assertEquals(1, adapter.unmarshal("1"));
		assertEquals(1d, adapter.unmarshal("1.0"));
	}

	@Test
	public void marshal(){
		NumberAdapter adapter = new NumberAdapter();

		assertEquals("1", adapter.marshal(1));
		assertEquals("1.0", adapter.marshal(1d));
	}
}