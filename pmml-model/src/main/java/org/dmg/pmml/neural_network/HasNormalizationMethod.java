/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.dmg.pmml.neural_network;

import org.dmg.pmml.PMMLObject;

public interface HasNormalizationMethod<E extends PMMLObject & HasNormalizationMethod<E>> {

	default
	NeuralNetwork.NormalizationMethod getNormalizationMethod(NeuralNetwork.NormalizationMethod defaultNormalizationMethod){
		NeuralNetwork.NormalizationMethod normalizationMethod = getNormalizationMethod();

		if(normalizationMethod == null){
			return defaultNormalizationMethod;
		}

		return normalizationMethod;
	}

	NeuralNetwork.NormalizationMethod getNormalizationMethod();

	E setNormalizationMethod(NeuralNetwork.NormalizationMethod normalizationMethod);
}