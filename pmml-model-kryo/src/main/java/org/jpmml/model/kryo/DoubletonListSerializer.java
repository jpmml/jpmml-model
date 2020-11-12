/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jpmml.model.collections.DoubletonList;

public class DoubletonListSerializer extends AbstractFixedSizeListSerializer<DoubletonList<?>> {

	@Override
	public void write(Kryo kryo, Output output, DoubletonList<?> doubletonList){
		Object first = doubletonList.get(0);
		Object second = doubletonList.get(1);

		kryo.writeClassAndObject(output, first);
		kryo.writeClassAndObject(output, second);
	}

	@Override
	public DoubletonList<?> read(Kryo kryo, Input input, Class<? extends DoubletonList<?>> clazz){
		Object first = kryo.readClassAndObject(input);
		Object second = kryo.readClassAndObject(input);

		return new DoubletonList<>(first, second);
	}
}