/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import org.junit.After;
import org.junit.Before;

abstract
public class KryoUtilTest {

	private Kryo kryo = null;


	@Before
	public void setUp(){
		Kryo kryo = new Kryo();

		KryoUtil.init(kryo);
		KryoUtil.register(kryo);

		this.kryo = kryo;
	}

	@After
	public void tearDown(){
		this.kryo = null;
	}

	public <E> E clone(E object){
		return KryoUtil.clone(this.kryo, object);
	}
}