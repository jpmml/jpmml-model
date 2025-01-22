/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FieldNameAdapterTest {

	@Test
	public void unmarshal(){
		FieldNameAdapter adapter = new FieldNameAdapter();

		assertThrows(NullPointerException.class, () -> adapter.unmarshal(null));
		assertThrows(IllegalArgumentException.class, () -> adapter.unmarshal(""));

		assertNotNull(adapter.unmarshal("x"));
	}

	@Test
	public void marshal(){
		FieldNameAdapter adapter = new FieldNameAdapter();

		assertEquals(null, adapter.marshal(null));
		assertEquals("x", adapter.marshal("x"));
	}
}