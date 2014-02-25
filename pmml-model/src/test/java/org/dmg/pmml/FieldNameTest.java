/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import org.jpmml.model.*;

import org.junit.*;

import static org.junit.Assert.*;

public class FieldNameTest {

	@Test
	public void create(){
		assertSame(FieldName.create("x"), FieldName.create("x"));
	}

	@Test
	public void serialization() throws Exception {
		FieldName name = FieldName.create("x");

		assertSame(name, SerializationUtil.clone(name));
	}

	@Test
	public void unmarshal(){
		assertNotNull(FieldName.unmarshal("x"));
	}

	@Test
	public void marshal(){
		assertEquals("x", FieldName.marshal(FieldName.create("x")));
		assertEquals(null, FieldName.marshal(null));
	}
}