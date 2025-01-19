/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.io.IOException;
import java.io.InputStream;

import com.esotericsoftware.kryo.Kryo;
import org.jpmml.model.DirectByteArrayOutputStream;
import org.jpmml.model.SerializerTest;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

abstract
public class KryoSerializerTest extends SerializerTest {

	protected Kryo kryo = null;


	@Before
	public void setUp(){
		this.kryo = KryoUtil.createKryo();
	}

	@After
	public void tearDown(){
		this.kryo = null;
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

			assertEquals(-1, is.read());

			return clonedObject;
		}
	}
}