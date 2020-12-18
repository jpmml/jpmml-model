/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jpmml.model.SerializationUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class FieldNameTest {

	@Test
	public void create(){
		FieldName name = FieldName.create("x");

		FieldName createdName;

		ConcurrentMap<String, FieldName> cache = new ConcurrentHashMap<>();

		try {
			FieldName.CACHE_PROVIDER.set(cache);

			createdName = FieldName.create("x");

			assertEquals(name, createdName);
			assertNotSame(name, createdName);
		} finally {
			FieldName.CACHE_PROVIDER.remove();
		}

		assertEquals(1, cache.size());

		createdName = FieldName.create(new String("x"));

		assertSame(name, createdName);
	}

	@Test
	public void readResolve() throws Exception {
		FieldName name = FieldName.create("x");

		FieldName clonedName;

		ConcurrentMap<String, FieldName> cache = new ConcurrentHashMap<>();

		try {
			FieldName.CACHE_PROVIDER.set(cache);

			clonedName = SerializationUtil.clone(name);

			assertEquals(name, clonedName);
			assertNotSame(name, clonedName);
		} finally {
			FieldName.CACHE_PROVIDER.remove();
		}

		assertEquals(1, cache.size());

		clonedName = SerializationUtil.clone(name);

		assertSame(name, clonedName);
	}
}