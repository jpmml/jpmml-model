/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.dmg.pmml.neural_network;

import java.lang.reflect.Field;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NeuralLayerTest {

	@Test
	public void activationFunctionType() throws NoSuchFieldException {
		Field activationFunction = NeuralLayer.class.getDeclaredField("activationFunction");
		Field networkActivationFunction = NeuralNetwork.class.getDeclaredField("activationFunction");

		assertEquals(networkActivationFunction.getType(), activationFunction.getType());
	}

	@Test
	public void normalizationMethodType() throws NoSuchFieldException {
		Field normalizationMethod = NeuralLayer.class.getDeclaredField("normalizationMethod");
		Field networkNormalizationMethod = NeuralNetwork.class.getDeclaredField("normalizationMethod");

		assertEquals(networkNormalizationMethod.getType(), normalizationMethod.getType());
	}
}