/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.neural_network;

import jakarta.xml.bind.annotation.XmlTransient;
import org.dmg.pmml.Entity;

@XmlTransient
abstract
public class NeuralEntity extends Entity<String> {

	abstract
	public String requireId();
}