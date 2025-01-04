/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml.time_series;

import java.util.List;

import org.dmg.pmml.PMMLObject;

public interface HasDynamicRegressors<E extends PMMLObject & HasDynamicRegressors<E>> {

	boolean hasDynamicRegressors();

	List<DynamicRegressor> getDynamicRegressors();

	E addDynamicRegressors(DynamicRegressor... dynamicRegressors);
}