/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.dmg.pmml.neural_network;

import org.dmg.pmml.PMMLObject;

public interface HasActivationFunction<E extends PMMLObject & HasActivationFunction<E>> {

	default
	NeuralNetwork.ActivationFunction getActivationFunction(NeuralNetwork.ActivationFunction defaultActivationFunction){
		NeuralNetwork.ActivationFunction activationFunction = getActivationFunction();

		if(activationFunction == null){
			return defaultActivationFunction;
		}

		return activationFunction;
	}

	NeuralNetwork.ActivationFunction getActivationFunction();

	E setActivationFunction(NeuralNetwork.ActivationFunction activationFunction);

	/**
	 * @see NeuralNetwork.ActivationFunction#THRESHOLD
	 */
	Number getThreshold();

	E setThreshold(Number threshold);

	/**
	 * @see NeuralNetwork.ActivationFunction#RECTIFIER
	 */
	Number getLeakage();

	E setLeakage(Number leakage);

	/**
	 * @see NeuralNetwork.ActivationFunction#RADIAL_BASIS
	 */
	Number getWidth();

	E setWidth(Number width);

	/**
	 * @see NeuralNetwork.ActivationFunction#RADIAL_BASIS
	 */
	Number getAltitude();

	E setAltitude(Number altitude);
}