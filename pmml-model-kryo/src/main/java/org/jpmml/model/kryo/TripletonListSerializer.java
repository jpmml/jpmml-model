/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jpmml.model.collections.TripletonList;

public class TripletonListSerializer extends AbstractImmutableListSerializer<TripletonList<?>> {

	@Override
	public void write(Kryo kryo, Output output, TripletonList<?> tripletonList){
		Object first = tripletonList.get(0);
		Object second = tripletonList.get(1);
		Object third = tripletonList.get(2);

		kryo.writeClassAndObject(output, first);
		kryo.writeClassAndObject(output, second);
		kryo.writeClassAndObject(output, third);
	}

	@Override
	public TripletonList<?> read(Kryo kryo, Input input, Class<? extends TripletonList<?>> clazz){
		Object first = kryo.readClassAndObject(input);
		Object second = kryo.readClassAndObject(input);
		Object third = kryo.readClassAndObject(input);

		return new TripletonList<>(first, second, third);
	}
}