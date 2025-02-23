/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.dmg.pmml.PMML;
import org.dmg.pmml.ScoreDistributionTransformer;
import org.dmg.pmml.SimplifyingScoreDistributionTransformer;
import org.jpmml.model.resources.ScoreDistributionPolymorphismTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ScoreDistributionAdapterTest {

	@Test
	public void checkDefault(){
		ScoreDistributionTransformer defaultScoreDistributionTransformer = ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.get();

		assertSame(SimplifyingScoreDistributionTransformer.INSTANCE, defaultScoreDistributionTransformer);
	}

	@Test
	public void loadComplex() throws Exception {
		PMML pmml = ScoreDistributionPolymorphismTest.load(null);

		ScoreDistributionPolymorphismTest.checkComplex(pmml);
	}

	@Test
	public void loadSimplified() throws Exception {
		PMML pmml = ScoreDistributionPolymorphismTest.load(SimplifyingScoreDistributionTransformer.INSTANCE);

		ScoreDistributionPolymorphismTest.checkSimplified(pmml);
	}
}