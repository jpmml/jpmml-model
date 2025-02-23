/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import java.io.InputStream;

import org.dmg.pmml.PMML;
import org.dmg.pmml.ScoreDistributionTransformer;
import org.dmg.pmml.SimplifyingScoreDistributionTransformer;
import org.jpmml.model.PMMLUtil;
import org.jpmml.model.resources.ResourceUtil;
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
		PMML pmml = load(null);

		ScoreDistributionPolymorphismTest.checkComplex(pmml);
	}

	@Test
	public void loadSimplified() throws Exception {
		PMML pmml = load(SimplifyingScoreDistributionTransformer.INSTANCE);

		ScoreDistributionPolymorphismTest.checkSimplified(pmml);
	}

	static
	private PMML load(ScoreDistributionTransformer scoreDistributionTransormer) throws Exception {
		ScoreDistributionTransformer defaultScoreDistributionTransformer = ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.get();

		try(InputStream is = ResourceUtil.getStream(ScoreDistributionPolymorphismTest.class)){
			ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.set(scoreDistributionTransormer);

			return PMMLUtil.unmarshal(is);
		} finally {
			ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.set(defaultScoreDistributionTransformer);
		}
	}
}