/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class PredictorList extends PMMLObject implements HasExtensions {

	abstract
	public boolean hasPredictors();

	abstract
	public List<Predictor> getPredictors();
}