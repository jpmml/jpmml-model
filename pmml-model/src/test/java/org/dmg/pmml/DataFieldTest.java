/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataFieldTest {

	@Test
	public void addValues(){
		DataField dataField = new DataField()
			.addValues(null, 1, 2, 3)
			.addValues(Value.Property.MISSING, "N/A");

		List<Value> values = dataField.getValues();

		assertEquals(4, values.size());

		for(int i = 0; i < 3; i++){
			checkValue(Value.Property.VALID, (i + 1), values.get(i));
		}

		checkValue(Value.Property.MISSING, "N/A", values.get(3));
	}

	static
	private void checkValue(Value.Property property, Object value, Value pmmlValue){
		assertEquals(property, pmmlValue.getProperty());
		assertEquals(value, pmmlValue.getValue());
	}
}