/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import org.dmg.pmml.DataField;
import org.dmg.pmml.DataType;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.OpType;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class FieldNameSerializerTest extends KryoUtilTest {

	@Test
	public void kryoClone(){
		DataField dataField = new DataField(null, OpType.CONTINUOUS, DataType.DOUBLE);

		DataField clonedDataField = clone(dataField);

		assertNotSame(dataField, clonedDataField);

		assertNull(clonedDataField.getName());

		dataField.setName(FieldName.create("x"));

		clonedDataField = clone(dataField);

		assertNotSame(dataField, clonedDataField);

		assertSame(dataField.getName(), clonedDataField.getName());
	}
}