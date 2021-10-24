/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml.general_regression;

import java.util.List;

import jakarta.xml.bind.annotation.XmlTransient;
import org.dmg.pmml.PMMLObject;

@XmlTransient
abstract
public class PredictorList extends PMMLObject {

	abstract
	public boolean hasPredictors();

	abstract
	public List<Predictor> getPredictors();

	abstract
	public PredictorList addPredictors(Predictor... predictors);
}