/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.io.IOException;
import java.io.InputStream;

import com.esotericsoftware.kryo.Kryo;
import org.dmg.pmml.PMMLObject;
import org.jpmml.model.DirectByteArrayOutputStream;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.Serializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract
public class KryoSerializerTest {

	protected Kryo kryo = null;


	@BeforeEach
	public void setUp(){
		this.kryo = KryoUtil.createKryo();
	}

	@AfterEach
	public void tearDown(){
		this.kryo = null;
	}

	static
	protected <E extends PMMLObject> E checkedClone(Serializer serializer, E object) throws Exception {
		E clonedObject = SerializationUtil.clone(serializer, object);

		assertTrue(ReflectionUtil.equals(object, clonedObject));

		return clonedObject;
	}

	static
	protected Object checkedCloneRaw(KryoSerializer kryoSerializer, Object object) throws Exception {
		Object clonedObject = cloneRaw(kryoSerializer, object);

		assertEquals(object, clonedObject);
		assertNotSame(object, clonedObject);

		return clonedObject;
	}

	static
	protected <E> E cloneRaw(KryoSerializer serializer, Object object) throws IOException {
		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(10 * 1024);

		serializer.serializeRaw(object, buffer);

		try(InputStream is = buffer.getInputStream()){
			@SuppressWarnings("unchecked")
			E clonedObject = (E)serializer.deserializeRaw(is);

			if(is.read() != -1){
				throw new IOException();
			}

			return clonedObject;
		}
	}
}