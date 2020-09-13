/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.io.Serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMMLObject;
import org.jpmml.model.DirectByteArrayOutputStream;
import org.jpmml.model.collections.DoubletonList;

public class KryoUtil {

	private KryoUtil(){
	}

	static
	public void register(Kryo kryo){
		// org.dmg.pmml.*
		kryo.register(FieldName.class, new FieldNameSerializer());

		kryo.addDefaultSerializer(PMMLObject.class, PMMLObjectSerializer.class);

		// org.jpmml.model.*
		kryo.register(DoubletonList.class, new DoubletonListSerializer());
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	static
	public <S extends Serializable> S clone(Kryo kryo, S object){
		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(1024 * 1024);

		try(Output output = new Output(buffer)){
			kryo.writeClassAndObject(output, object);
		}

		try(Input input = new Input(buffer.getInputStream())){
			return (S)kryo.readClassAndObject(input);
		}
	}
}