/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml.scorecard;

import org.dmg.pmml.PMMLObject;

/**
 * <p>
 * A marker interface for PMML elements that specify the <code>baselineScore</code> attribute.
 * </p>
 */
public interface HasBaselineScore<E extends PMMLObject & HasBaselineScore<E>> {

	default
	Number getBaselineScore(Number defaultBaselineScore){
		Number baselineScore = getBaselineScore();

		if(baselineScore == null){
			return defaultBaselineScore;
		}

		return baselineScore;
	}

	Number getBaselineScore();

	E setBaselineScore(Number baselineScore);
}