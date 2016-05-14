/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.dmg.pmml.PMML;

public class SerializationUtil {

	private SerializationUtil(){
	}

	static
	public PMML deserializePMML(InputStream is) throws ClassNotFoundException, IOException {
		return (PMML)deserialize(is);
	}

	static
	public Object deserialize(InputStream is) throws ClassNotFoundException, IOException {
		FilterInputStream safeIs = new FilterInputStream(is){

			@Override
			public void close(){
			}
		};

		try(ObjectInputStream ois = new ObjectInputStream(safeIs)){
			return ois.readObject();
		}
	}

	static
	public void serializePMML(PMML pmml, OutputStream os) throws IOException {
		serialize(pmml, os);
	}

	static
	public <S extends Serializable> void serialize(S object, OutputStream os) throws IOException {
		FilterOutputStream safeOs = new FilterOutputStream(os){

			@Override
			public void close() throws IOException {
				super.flush();
			}
		};

		try(ObjectOutputStream oos = new ObjectOutputStream(safeOs)){
			oos.writeObject(object);

			oos.flush();
		}
	}

	static
	public <S extends Serializable> S clone(S object) throws ClassNotFoundException, IOException {
		return clone(object, 1024 * 1024);
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	static
	public <S extends Serializable> S clone(S object, int capacity) throws ClassNotFoundException, IOException {
		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(capacity);

		serialize(object, buffer);

		try(InputStream is = buffer.getInputStream()){
			return (S)deserialize(is);
		}
	}
}