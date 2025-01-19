/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.dmg.pmml.PMMLObject;
import org.jpmml.model.Serializer;

public class KryoSerializer implements Serializer {

	private Kryo kryo = null;


	public KryoSerializer(){
		this(KryoUtil.createKryo());
	}

	public KryoSerializer(Kryo kryo){
		setKryo(kryo);
	}

	@Override
	public PMMLObject deserialize(InputStream is){
		return (PMMLObject)deserializeRaw(is);
	}

	@Override
	public void serialize(PMMLObject object, OutputStream os){
		serializeRaw(object, os);
	}

	public Object deserializeRaw(InputStream is){
		Kryo kryo = getKryo();

		try(Input input = new Input(is)){
			return kryo.readClassAndObject(input);
		}
	}

	public void serializeRaw(Object object, OutputStream os){
		Kryo kryo = getKryo();

		try(Output output = new Output(os)){
			kryo.writeClassAndObject(output, object);
		}
	}

	public Kryo getKryo(){
		return this.kryo;
	}

	private void setKryo(Kryo kryo){
		this.kryo = Objects.requireNonNull(kryo);
	}
}