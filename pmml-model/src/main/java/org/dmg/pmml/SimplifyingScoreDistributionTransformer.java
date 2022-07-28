/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

public class SimplifyingScoreDistributionTransformer implements ScoreDistributionTransformer {

	@Override
	public ScoreDistribution fromComplexScoreDistribution(ComplexScoreDistribution complexScoreDistribution){
		return simplify(complexScoreDistribution);
	}

	@Override
	public ComplexScoreDistribution toComplexScoreDistribution(ScoreDistribution scoreDistribution){
		return scoreDistribution.toComplexScoreDistribution();
	}

	private ScoreDistribution simplify(ScoreDistribution scoreDistribution){

		if(scoreDistribution.hasExtensions()){
			return scoreDistribution;
		} // End if

		if(scoreDistribution.getConfidence() != null){
			return scoreDistribution;
		} // End if

		if(scoreDistribution.getProbability() != null){
			return new ScoreProbability(scoreDistribution);
		} else

		{
			return new ScoreFrequency(scoreDistribution);
		}
	}

	public static final SimplifyingScoreDistributionTransformer INSTANCE = new SimplifyingScoreDistributionTransformer();
}