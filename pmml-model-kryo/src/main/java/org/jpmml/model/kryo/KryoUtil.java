/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import jakarta.xml.bind.JAXBElement;
import org.dmg.pmml.PMMLObject;
import org.jpmml.model.collections.DoubletonList;
import org.jpmml.model.collections.SingletonList;
import org.jpmml.model.collections.TripletonList;
import org.jpmml.model.kryo.serializers.DoubletonListSerializer;
import org.jpmml.model.kryo.serializers.PMMLObjectSerializer;
import org.jpmml.model.kryo.serializers.SingletonListSerializer;
import org.jpmml.model.kryo.serializers.TripletonListSerializer;
import org.w3c.dom.Element;

public class KryoUtil {

	private KryoUtil(){
	}

	static
	public Kryo createKryo(){
		Kryo kryo = new Kryo();

		init(kryo);
		register(kryo);

		return kryo;
	}

	static
	public void init(Kryo kryo){
		kryo.setRegistrationRequired(false);
		kryo.setReferences(true);
	}

	static
	public void register(Kryo kryo){
		// org.dmg.pmml.*
		kryo.addDefaultSerializer(PMMLObject.class, PMMLObjectSerializer.class);

		// Custom XML elements (eg. InlineTable rows)
		kryo.addDefaultSerializer(Element.class, new JavaSerializer());
		kryo.addDefaultSerializer(JAXBElement.class, new JavaSerializer());

		// org.jpmml.model.collections.*
		kryo.register(SingletonList.class, new SingletonListSerializer());
		kryo.register(DoubletonList.class, new DoubletonListSerializer());
		kryo.register(TripletonList.class, new TripletonListSerializer());
	}
}