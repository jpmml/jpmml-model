/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.jpmml.model.collections.SingletonList;

public class SingletonListSerializer extends AbstractFixedSizeListSerializer<SingletonList<?>> {

	@Override
	public void write(Kryo kryo, Output output, SingletonList<?> singletonList){
		Object element = singletonList.get(0);

		kryo.writeClassAndObject(output, element);
	}

	@Override
	public SingletonList<?> read(Kryo kryo, Input input, Class<? extends SingletonList<?>> clazz){
		Object element = kryo.readClassAndObject(input);

		return new SingletonList<>(element);
	}
}