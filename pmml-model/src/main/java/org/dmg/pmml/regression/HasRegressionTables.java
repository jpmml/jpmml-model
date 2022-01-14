/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml.regression;

import java.util.List;

import org.dmg.pmml.PMMLObject;

public interface HasRegressionTables<E extends PMMLObject & HasRegressionTables<E>> {

	RegressionModel.NormalizationMethod getNormalizationMethod();

	E setNormalizationMethod(RegressionModel.NormalizationMethod normalizationMethod);

	boolean hasRegressionTables();

	List<RegressionTable> requireRegressionTables();

	List<RegressionTable> getRegressionTables();

	E addRegressionTables(RegressionTable... regressionTables);
}