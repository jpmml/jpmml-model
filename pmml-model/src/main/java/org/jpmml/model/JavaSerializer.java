/*
 * Copyright (c) 2025 Villu Ruusmann
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

import org.dmg.pmml.PMMLObject;

public class JavaSerializer implements Serializer {

	private ClassLoader clazzLoader = null;


	public JavaSerializer(){
		this(null);
	}

	public JavaSerializer(ClassLoader clazzLoader){
		setClassLoader(clazzLoader);
	}

	@Override
	public PMMLObject deserialize(InputStream is) throws ClassNotFoundException, IOException {
		ClassLoader clazzLoader = getClassLoader();

		FilterInputStream safeIs = new FilterInputStream(is){

			@Override
			public void close(){
			}
		};

		try(ObjectInputStream objectIs = new ObjectInputStream(safeIs){

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
			return (PMMLObject)objectIs.readObject();
		}
	}

	@Override
	public void serialize(PMMLObject object, OutputStream os) throws IOException {
		FilterOutputStream safeOs = new FilterOutputStream(os){

			@Override
			public void close() throws IOException {
				super.flush();
			}
		};

		try(ObjectOutputStream objectOs = new ObjectOutputStream(safeOs)){
			objectOs.writeObject(object);

			objectOs.flush();
		}
	}

	protected ClassLoader getClassLoader(){
		return this.clazzLoader;
	}

	private void setClassLoader(ClassLoader clazzLoader){
		this.clazzLoader = clazzLoader;
	}
}