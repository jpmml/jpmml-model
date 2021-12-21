/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml.regression;

import jakarta.xml.bind.annotation.XmlTransient;
import org.dmg.pmml.PMMLObject;

@XmlTransient
abstract
public class Term extends PMMLObject {

	abstract
	public Number requireCoefficient();

	abstract
	public Number getCoefficient();

	abstract
	public Term setCoefficient(Number coefficient);
}