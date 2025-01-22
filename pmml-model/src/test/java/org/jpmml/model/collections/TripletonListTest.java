/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.collections;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TripletonListTest extends AbstractFixedSizeListTest {

	@Test
	public void readContract(){
		List<String> list = new TripletonList<>("first", "second", "third");

		assertTrue(list instanceof Serializable);

		assertEquals(3, list.size());
		assertFalse(list.isEmpty());

		assertEquals("first", list.get(0));
		assertEquals("second", list.get(1));
		assertEquals("third", list.get(2));

		try {
			list.get(3);

			fail();
		} catch(IndexOutOfBoundsException ioobe){
			// Ignored
		}

		assertEquals(Collections.emptyList(), list.subList(0, 0));
		assertEquals(Arrays.asList("first"), list.subList(0, 1));
		assertEquals(Arrays.asList("first", "second"), list.subList(0, 2));
		assertEquals(Arrays.asList("first", "second", "third"), list.subList(0, 3));
		assertEquals(Arrays.asList("second"), list.subList(1, 2));
		assertEquals(Arrays.asList("second", "third"), list.subList(1, 3));
		assertEquals(Arrays.asList("third"), list.subList(2, 3));
		assertEquals(Collections.emptyList(), list.subList(3, 3));
	}

	@Test
	public void updateContract(){
		List<String> list = new TripletonList<>("first", "second", "third");

		list.set(0, "First");
		list.set(1, "Second");
		list.set(2, "Third");

		try {
			list.set(3, "Fourth");

			fail();
		} catch(IndexOutOfBoundsException ioobe){
			// Ignored
		}

		assertEquals(Arrays.asList("First", "Second", "Third"), list);

		transform(list, string -> string.toUpperCase());

		assertEquals(Arrays.asList("FIRST", "SECOND", "THIRD"), list);
	}
}