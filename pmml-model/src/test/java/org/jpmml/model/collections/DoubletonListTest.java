/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.collections;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DoubletonListTest extends AbstractImmutableListTest {

	@Test
	public void readContract(){
		List<String> list = new DoubletonList<>("first", "second");

		assertTrue(list instanceof Serializable);

		assertEquals(2, list.size());
		assertFalse(list.isEmpty());

		assertEquals("first", list.get(0));
		assertEquals("second", list.get(1));

		try {
			list.get(2);

			fail();
		} catch(IndexOutOfBoundsException ioobe){
			// Ignored
		}

		assertEquals(0, list.indexOf("first"));
		assertEquals(1, list.indexOf("second"));
		assertEquals(-1, list.indexOf(null));

		assertEquals(0, list.lastIndexOf("first"));
		assertEquals(1, list.lastIndexOf("second"));
		assertEquals(-1, list.lastIndexOf(null));

		assertEquals(Collections.emptyList(), list.subList(0, 0));
		assertEquals(Arrays.asList("first"), list.subList(0, 1));
		assertEquals(Arrays.asList("first", "second"), list.subList(0, 2));
		assertEquals(Arrays.asList("second"), list.subList(1, 2));
		assertEquals(Collections.emptyList(), list.subList(2, 2));

		assertEquals(Arrays.asList("first", "second"), toList(list.iterator()));

		assertEquals(Arrays.asList("first", "second"), toList(list.listIterator(0)));
		assertEquals(Arrays.asList("second"), toList(list.listIterator(1)));
		assertEquals(Collections.emptyList(), toList(list.listIterator(2)));
	}

	@Test
	public void updateContract(){
		List<String> list = new DoubletonList<>("first", "second");

		assertEquals("first", list.set(0, "First"));
		assertEquals("second", list.set(1, "Second"));

		try {
			list.set(2, "Third");

			fail();
		} catch(IndexOutOfBoundsException ioobe){
			// Ignored
		}

		assertEquals(Arrays.asList("First", "Second"), list);

		transform(list, string -> string.toUpperCase());

		assertEquals(Arrays.asList("FIRST", "SECOND"), list);
	}
}