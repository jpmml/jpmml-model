/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;

import org.dmg.pmml.IntSparseArray;
import org.dmg.pmml.SparseArray;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class IntegerInternerTest {

	@Test
	@SuppressWarnings({"deprecation", "removal"})
	public void intern(){
		SparseArray<Integer> sparseArray = new IntSparseArray()
			.setN(new Integer(3))
			.addIndices(new Integer(1), new Integer(2), new Integer(3))
			.setDefaultValue(new Integer(0))
			.addEntries(new Integer(0), new Integer(0), new Integer(0));

		List<Integer> indices = sparseArray.getIndices();

		assertNotSame(indices.get(2), sparseArray.getN());

		List<Integer> entries = sparseArray.getEntries();

		for(Integer entry : entries){
			assertNotSame(sparseArray.getDefaultValue(), entry);
		}

		IntegerInterner interner = new IntegerInterner();
		interner.applyTo(sparseArray);

		assertSame(indices.get(2), sparseArray.getN());

		for(Integer entry : entries){
			assertSame(sparseArray.getDefaultValue(), entry);
		}
	}
}