/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.dmg.pmml.neural_network;

import org.dmg.pmml.PMMLObject;

public interface HasNormalizationMethod<E extends PMMLObject & HasNormalizationMethod<E>> {

	NeuralNetwork.NormalizationMethod getNormalizationMethod();

	E setNormalizationMethod(NeuralNetwork.NormalizationMethod normalizationMethod);
}