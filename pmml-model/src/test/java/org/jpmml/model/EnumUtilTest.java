/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.DataField;
import org.dmg.pmml.PMMLAttributes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumUtilTest {

	@Test
	public void getEnumField(){
		DataField dataField = new DataField();

		assertEquals(PMMLAttributes.DATAFIELD_CYCLIC, EnumUtil.getEnumField(dataField, DataField.Cyclic.ZERO));
	}

	@Test
	public void getEnumValue(){
		DataField.Cyclic zero = DataField.Cyclic.ZERO;

		assertEquals("ZERO", zero.name());
		assertEquals("0", zero.value());

		assertEquals("0", EnumUtil.getEnumValue(zero));
	}
}