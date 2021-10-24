/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import jakarta.xml.bind.JAXBElement;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMMLObject;
import org.jpmml.model.DirectByteArrayOutputStream;
import org.jpmml.model.collections.DoubletonList;
import org.jpmml.model.collections.SingletonList;
import org.jpmml.model.collections.TripletonList;
import org.w3c.dom.Element;

public class KryoUtil {

	private KryoUtil(){
	}

	static
	public void init(Kryo kryo){
		kryo.setRegistrationRequired(false);
		kryo.setReferences(true);
	}

	static
	public void register(Kryo kryo){
		// org.dmg.pmml.*
		kryo.register(FieldName.class, new FieldNameSerializer());

		kryo.addDefaultSerializer(PMMLObject.class, PMMLObjectSerializer.class);

		// Custom XML elements (eg. InlineTable rows)
		kryo.addDefaultSerializer(Element.class, new JavaSerializer());
		kryo.addDefaultSerializer(JAXBElement.class, new JavaSerializer());

		// org.jpmml.model.*
		kryo.register(SingletonList.class, new SingletonListSerializer());
		kryo.register(DoubletonList.class, new DoubletonListSerializer());
		kryo.register(TripletonList.class, new TripletonListSerializer());
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	static
	public <E> E clone(Kryo kryo, E object){
		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(1024 * 1024);

		try(Output output = new Output(buffer)){
			kryo.writeClassAndObject(output, object);
		}

		try(Input input = new Input(buffer.getInputStream())){
			return (E)kryo.readClassAndObject(input);
		}
	}
}