/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import org.dmg.pmml.DataField;
import org.dmg.pmml.DataType;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.OpType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class FieldNameSerializerTest {

	private Kryo kryo = null;


	@Before
	public void setUp(){
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(false);

		KryoUtil.register(kryo);

		this.kryo = kryo;
	}

	@Test
	public void kryoClone(){
		DataField dataField = new DataField(null, OpType.CONTINUOUS, DataType.DOUBLE);

		DataField clonedDataField = KryoUtil.clone(this.kryo, dataField);

		assertNotSame(dataField, clonedDataField);

		assertNull(clonedDataField.getName());

		dataField.setName(FieldName.create("x"));

		clonedDataField = KryoUtil.clone(this.kryo, dataField);

		assertNotSame(dataField, clonedDataField);

		assertSame(dataField.getName(), clonedDataField.getName());
	}
}