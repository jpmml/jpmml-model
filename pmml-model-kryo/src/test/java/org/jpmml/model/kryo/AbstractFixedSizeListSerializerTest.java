/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.util.ArrayList;
import java.util.Arrays;

import com.esotericsoftware.kryo.Kryo;

import org.jpmml.model.collections.DoubletonList;
import org.jpmml.model.collections.SingletonList;
import org.jpmml.model.collections.TripletonList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractFixedSizeListSerializerTest {

	private Kryo kryo = null;


	@Before
	public void setUp(){
		Kryo kryo = new Kryo();

		KryoUtil.init(kryo);
		KryoUtil.register(kryo);

		this.kryo = kryo;
	}

	@Test
	public void collections(){
		assertEquals(Arrays.asList(), KryoUtil.clone(this.kryo, new ArrayList<>()));

		assertEquals(Arrays.asList("first"), KryoUtil.clone(this.kryo, new SingletonList<>("first")));
		assertEquals(Arrays.asList("first", "second"), KryoUtil.clone(this.kryo, new DoubletonList<>("first", "second")));
		assertEquals(Arrays.asList("first", "second", "third"), KryoUtil.clone(this.kryo, new TripletonList<>("first", "second", "third")));
	}
}