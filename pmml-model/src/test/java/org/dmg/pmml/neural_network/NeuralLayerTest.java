/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.dmg.pmml.neural_network;

import org.dmg.pmml.ReflectionTest;
import org.junit.jupiter.api.Test;

public class NeuralLayerTest extends ReflectionTest {

	@Test
	public void activationFunctionType() throws NoSuchFieldException {
		checkField(NeuralNetwork.class, NeuralLayer.class, "activationFunction");
	}

	@Test
	public void normalizationMethodType() throws NoSuchFieldException {
		checkField(NeuralNetwork.class, NeuralLayer.class, "normalizationMethod");
	}
}