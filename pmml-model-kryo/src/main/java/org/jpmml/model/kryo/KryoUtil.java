/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import org.dmg.pmml.FieldName;

public class KryoUtil {

	private KryoUtil(){
	}

	static
	public void register(Kryo kryo){
		kryo.register(FieldName.class, new JavaSerializer());
	}
}