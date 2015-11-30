/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtil {

	private SerializationUtil(){
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	static
	public <V> V clone(V object) throws Exception {
		return (V)deserializeObject(serializeObject(object));
	}

	static
	public byte[] serializeObject(Object object) throws Exception {

		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){

			try(ObjectOutputStream oos = new ObjectOutputStream(os)){
				oos.writeObject(object);
				oos.flush();

				return os.toByteArray();
			}
		}
	}

	static
	public Object deserializeObject(byte[] bytes) throws Exception {

		try(ByteArrayInputStream is = new ByteArrayInputStream(bytes)){

			try(ObjectInputStream ois = new ObjectInputStream(is)){
				return ois.readObject();
			}
		}
	}
}