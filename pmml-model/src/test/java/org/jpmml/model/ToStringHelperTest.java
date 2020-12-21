/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToStringHelperTest {

	@Test
	public void append(){
		ToStringHelper toStringHelper = new ToStringHelper(new HashMap<>());

		assertEquals("HashMap{}", toStringHelper.toString());

		toStringHelper.add("a", 1);

		assertEquals("HashMap{a=1}", toStringHelper.toString());

		toStringHelper.add("b", 2d);
		toStringHelper.add("c", 3);

		assertEquals("HashMap{a=1, b=2.0, c=3}", toStringHelper.toString());
	}
}