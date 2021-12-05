/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.util.ArrayList;
import java.util.Arrays;

import org.jpmml.model.collections.DoubletonList;
import org.jpmml.model.collections.SingletonList;
import org.jpmml.model.collections.TripletonList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CollectionTest extends KryoUtilTest {

	@Test
	public void kryoClone(){
		assertEquals(Arrays.asList(), clone(new ArrayList<>()));

		assertEquals(Arrays.asList("first"), clone(new SingletonList<>("first")));
		assertEquals(Arrays.asList("first", "second"), clone(new DoubletonList<>("first", "second")));
		assertEquals(Arrays.asList("first", "second", "third"), clone(new TripletonList<>("first", "second", "third")));
	}
}