/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.dmg.pmml.FieldName;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class FieldNameAdapterTest {

	@Test
	public void unmarshal(){
		FieldNameAdapter adapter = new FieldNameAdapter();

		try {
			adapter.unmarshal(null);

			fail();
		} catch(NullPointerException npe){
			// Ignored
		} // End try

		try {
			adapter.unmarshal("");

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}

		assertNotNull(adapter.unmarshal("x"));
	}

	@Test
	public void marshal(){
		FieldNameAdapter adapter = new FieldNameAdapter();

		assertEquals(null, adapter.marshal(null));
		assertEquals("x", adapter.marshal(FieldName.create("x")));
	}
}