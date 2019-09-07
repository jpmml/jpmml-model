/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.regression.NumericPredictor;
import org.dmg.pmml.regression.RegressionTable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldReferenceFinderTest {

	@Test
	public void apply(){
		RegressionTable regressionTable = new RegressionTable()
			.addNumericPredictors(new NumericPredictor(FieldName.create("x1"), 1d));

		FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder();
		fieldReferenceFinder.applyTo(regressionTable);

		assertEquals(Collections.singleton(FieldName.create("x1")), fieldReferenceFinder.getFieldNames());

		fieldReferenceFinder.reset();

		assertEquals(Collections.emptySet(), fieldReferenceFinder.getFieldNames());

		regressionTable
			.addNumericPredictors(new NumericPredictor(FieldName.create("x2"), -1d));

		fieldReferenceFinder.applyTo(regressionTable);

		assertEquals(new HashSet<>(Arrays.asList(FieldName.create("x1"), FieldName.create("x2"))), fieldReferenceFinder.getFieldNames());

		fieldReferenceFinder.reset();

		assertEquals(Collections.emptySet(), fieldReferenceFinder.getFieldNames());
	}
}