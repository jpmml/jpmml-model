/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.dmg.pmml.FieldName;

public class FieldNameSerializer extends Serializer<FieldName> {

	public FieldNameSerializer(){
		super(false, true);
	}

	@Override
	public void write(Kryo kryo, Output output, FieldName name){
		String value = name.getValue();

		output.writeString(value);
	}

	@Override
	public FieldName read(Kryo kryo, Input input, Class<? extends FieldName> clazz){
		String value = input.readString();

		return FieldName.create(value);
	}
}