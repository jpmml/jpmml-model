/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.IOException;
import java.io.InputStream;

import org.dmg.pmml.PMMLObject;

public class SerializationUtil {

	private SerializationUtil(){
	}

	static
	public <E extends PMMLObject> E clone(E object) throws ClassNotFoundException, IOException {
		return clone(object, null);
	}

	@SuppressWarnings("unchecked")
	static
	public <E extends PMMLObject> E clone(E object, ClassLoader clazzLoader) throws ClassNotFoundException, IOException {
		JavaSerializer serializer = new JavaSerializer(clazzLoader);

		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(1024 * 1024);

		serializer.serialize(object, buffer);

		try(InputStream is = buffer.getInputStream()){
			return (E)serializer.deserialize(is);
		}
	}
}