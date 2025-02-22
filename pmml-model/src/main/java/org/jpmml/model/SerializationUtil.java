/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.IOException;
import java.io.InputStream;

import org.dmg.pmml.PMMLObject;

public class SerializationUtil {

	private SerializationUtil(){
	}

	static
	public <E extends PMMLObject> E clone(Serializer serializer, E object) throws Exception {
		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(10 * 1024);

		serializer.serialize(object, buffer);

		try(InputStream is = buffer.getInputStream()){
			@SuppressWarnings("unchecked")
			E clonedObject = (E)serializer.deserialize(is);

			if(is.read() != -1){
				throw new IOException();
			}

			return clonedObject;
		}
	}
}