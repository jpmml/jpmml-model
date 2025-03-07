/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;

import org.dmg.pmml.RealSparseArray;
import org.dmg.pmml.SparseArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DoubleInternerTest {

	@Test
	@SuppressWarnings("deprecation")
	public void intern(){
		SparseArray<Double> sparseArray = new RealSparseArray()
			.setN(3)
			.addIndices(1, 2, 3)
			.setDefaultValue(new Double(1d))
			.addEntries(new Double(1d), new Double(1d), new Double(1d));

		List<Double> entries = sparseArray.getEntries();

		for(Double entry : entries){
			assertNotSame(sparseArray.getDefaultValue(), entry);
		}

		DoubleInterner interner = new DoubleInterner();
		interner.applyTo(sparseArray);

		for(Double entry : entries){
			assertSame(sparseArray.getDefaultValue(), entry);
		}
	}
}