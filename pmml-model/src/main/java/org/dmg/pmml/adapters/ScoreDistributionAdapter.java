/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.dmg.pmml.ComplexScoreDistribution;
import org.dmg.pmml.ScoreDistribution;
import org.dmg.pmml.ScoreDistributionTransformer;
import org.dmg.pmml.SimplifyingScoreDistributionTransformer;

public class ScoreDistributionAdapter extends XmlAdapter<ComplexScoreDistribution, ScoreDistribution> {

	@Override
	public ScoreDistribution unmarshal(ComplexScoreDistribution value){
		ScoreDistributionTransformer scoreDistributionTransformer = ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.get();

		if(scoreDistributionTransformer != null){
			return scoreDistributionTransformer.fromComplexScoreDistribution(value);
		}

		return value;
	}

	@Override
	public ComplexScoreDistribution marshal(ScoreDistribution scoreDistribution){
		ScoreDistributionTransformer scoreDistributionTransformer = ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.get();

		if(scoreDistributionTransformer != null){
			return scoreDistributionTransformer.toComplexScoreDistribution(scoreDistribution);
		}

		return scoreDistribution.toComplexScoreDistribution();
	}

	public static final ThreadLocal<ScoreDistributionTransformer> SCOREDISTRIBUTION_TRANSFORMER_PROVIDER = new ThreadLocal<ScoreDistributionTransformer>(){

		@Override
		public ScoreDistributionTransformer initialValue(){
			return SimplifyingScoreDistributionTransformer.INSTANCE;
		}
	};
}