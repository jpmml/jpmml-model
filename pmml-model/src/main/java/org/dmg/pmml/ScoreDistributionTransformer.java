/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

import org.dmg.pmml.adapters.ScoreDistributionAdapter;

/**
 * <p>
 * A {@link ScoreDistribution} element converter (two-way transformer) between the default representation ({@link ComplexScoreDistribution}) and a custom representation.
 * </p>
 *
 * @see ScoreDistributionAdapter
 * @see ScoreDistributionAdapter#SCOREDISTRIBUTION_TRANSFORMER_PROVIDER
 */
public interface ScoreDistributionTransformer {

	ScoreDistribution fromComplexScoreDistribution(ComplexScoreDistribution complexScoreDistribution);

	ComplexScoreDistribution toComplexScoreDistribution(ScoreDistribution scoreDistribution);
}