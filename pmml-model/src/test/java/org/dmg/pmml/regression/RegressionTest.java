/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml.regression;

import org.dmg.pmml.ReflectionTest;
import org.junit.jupiter.api.Test;

public class RegressionTest extends ReflectionTest {

	@Test
	public void normalizationMethodType() throws NoSuchFieldException {
		checkField(RegressionModel.class, Regression.class, "normalizationMethod");
	}
}