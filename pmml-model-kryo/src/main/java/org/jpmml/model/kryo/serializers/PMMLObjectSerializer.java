/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import org.dmg.pmml.PMMLObject;

public class PMMLObjectSerializer extends FieldSerializer<PMMLObject> {

	public PMMLObjectSerializer(Kryo kryo, Class<? extends PMMLObject> clazz){
		super(kryo, clazz);

		setAcceptsNull(false);
	}
}