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
import java.io.ObjectStreamClass;
import java.io.OutputStream;

import org.dmg.pmml.PMML;

public class SerializationUtil {

	private SerializationUtil(){
	}

	static
	public PMML deserializePMML(InputStream is) throws ClassNotFoundException, IOException {
		return deserializePMML(is, null);
	}

	static
	public PMML deserializePMML(InputStream is, ClassLoader clazzLoader) throws ClassNotFoundException, IOException {
		return (PMML)deserialize(is, clazzLoader);
	}

	static
	public Object deserialize(InputStream is) throws ClassNotFoundException, IOException {
		return deserialize(is, null);
	}

	static
	public Object deserialize(InputStream is, ClassLoader clazzLoader) throws ClassNotFoundException, IOException {
		FilterInputStream safeIs = new FilterInputStream(is){

			@Override
			public void close(){
			}
		};

		try(ObjectInputStream ois = new ObjectInputStream(safeIs){

			@Override
			public Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws ClassNotFoundException, IOException {

				if(clazzLoader != null){
					Class<?> clazz = Class.forName(objectStreamClass.getName(), false, clazzLoader);

					if(clazz != null){
						return clazz;
					}
				}

				return super.resolveClass(objectStreamClass);
			}
		}){
			return ois.readObject();
		}
	}

	static
	public void serializePMML(PMML pmml, OutputStream os) throws IOException {
		serialize(pmml, os);
	}

	static
	public void serialize(Object object, OutputStream os) throws IOException {
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
	public <E> E clone(E object) throws ClassNotFoundException, IOException {
		return clone(object, null);
	}

	@SuppressWarnings("unchecked")
	static
	public <E> E clone(E object, ClassLoader clazzLoader) throws ClassNotFoundException, IOException {
		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(1024 * 1024);

		serialize(object, buffer);

		try(InputStream is = buffer.getInputStream()){
			return (E)deserialize(is, clazzLoader);
		}
	}
}