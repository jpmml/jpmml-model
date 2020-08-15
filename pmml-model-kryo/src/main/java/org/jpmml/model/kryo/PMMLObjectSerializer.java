/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import org.dmg.pmml.PMMLObject;

public class PMMLObjectSerializer extends FieldSerializer<PMMLObject> {

	public PMMLObjectSerializer(Kryo kryo, Class clazz){
		super(kryo, clazz);

		setAcceptsNull(false);
	}
}