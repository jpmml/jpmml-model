/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.util.ArrayList;

import org.jpmml.model.collections.DoubletonList;
import org.jpmml.model.collections.SingletonList;
import org.jpmml.model.collections.TripletonList;
import org.junit.jupiter.api.Test;

public class CollectionTest extends KryoSerializerTest {

	@Test
	public void kryoClone() throws Exception {
		KryoSerializer kryoSerializer = new KryoSerializer(super.kryo);

		checkedCloneRaw(kryoSerializer, new ArrayList<>());

		checkedCloneRaw(kryoSerializer, new SingletonList<>("first"));
		checkedCloneRaw(kryoSerializer, new DoubletonList<>("first", "second"));
		checkedCloneRaw(kryoSerializer, new TripletonList<>("first", "second", "third"));
	}
}