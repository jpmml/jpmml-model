/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.dmg.pmml.general_regression;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeneralRegressionModelTest {

	@Test
	public void linkFunction(){
		GeneralRegressionModel.LinkFunction[] linkFunctions = GeneralRegressionModel.LinkFunction.values();

		assertEquals(10 + 2, linkFunctions.length);
	}
}