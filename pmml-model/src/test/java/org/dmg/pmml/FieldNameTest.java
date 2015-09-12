/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import org.jpmml.model.SerializationUtil;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FieldNameTest {

	@Test
	public void create(){
		assertSame(FieldName.create("x"), FieldName.create("x"));

		assertNotSame(new FieldName("x"), new FieldName("x"));
		assertNotSame(new FieldName("x"), FieldName.create("x"));
	}

	@Test
	public void readResolve() throws Exception {
		FieldName name = FieldName.create("x");
		assertTrue(name.isInterned());

		FieldName clonedName = SerializationUtil.clone(name);
		assertTrue(clonedName.isInterned());

		assertSame(name, clonedName);

		name = new FieldName("x");
		assertFalse(name.isInterned());

		clonedName = SerializationUtil.clone(name);
		assertTrue(clonedName.isInterned());

		assertNotSame(name, clonedName);
	}
}