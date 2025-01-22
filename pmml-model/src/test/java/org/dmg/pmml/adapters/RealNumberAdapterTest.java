/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RealNumberAdapterTest {

	@Test
	public void unmarshal(){
		RealNumberAdapter adapter = new RealNumberAdapter();

		assertEquals(Double.NaN, adapter.unmarshal("NaN"));
		assertEquals(Double.NEGATIVE_INFINITY, adapter.unmarshal("-INF"));
		assertEquals(Double.POSITIVE_INFINITY, adapter.unmarshal("INF"));

		assertThrows(IllegalArgumentException.class, () -> adapter.unmarshal("Infinity"));

		assertEquals(1, adapter.unmarshal("1"));
		assertEquals(1d, adapter.unmarshal("1.0"));
	}

	@Test
	public void marshal(){
		RealNumberAdapter adapter = new RealNumberAdapter();

		assertEquals("NaN", adapter.marshal(Float.NaN));
		assertEquals("-INF", adapter.marshal(Float.NEGATIVE_INFINITY));
		assertEquals("INF", adapter.marshal(Float.POSITIVE_INFINITY));

		assertEquals("NaN", adapter.marshal(Double.NaN));
		assertEquals("-INF", adapter.marshal(Double.NEGATIVE_INFINITY));
		assertEquals("INF", adapter.marshal(Double.POSITIVE_INFINITY));

		assertEquals("1", adapter.marshal(1));
		assertEquals("1.0", adapter.marshal(1f));
		assertEquals("1.0", adapter.marshal(1d));
	}
}