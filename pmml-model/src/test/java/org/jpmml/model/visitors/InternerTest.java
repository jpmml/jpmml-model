/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InternerTest {

	@Test
	public void isAssignableFrom(){
		assertTrue((Object.class).isAssignableFrom(String.class));
		assertFalse((Number.class).isAssignableFrom(String.class));

		assertTrue((Object.class).isAssignableFrom(Double.class));
		assertTrue((Number.class).isAssignableFrom(Double.class));
		assertFalse((Integer.class).isAssignableFrom(Double.class));
	}
}