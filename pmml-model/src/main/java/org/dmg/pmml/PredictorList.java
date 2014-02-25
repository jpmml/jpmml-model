/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlTransient
abstract
public class PredictorList extends PMMLObject {

	abstract
	public List<Predictor> getPredictors();
}