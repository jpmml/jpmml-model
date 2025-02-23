/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model.resources;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.ComplexScoreDistribution;
import org.dmg.pmml.PMML;
import org.dmg.pmml.ScoreDistribution;
import org.dmg.pmml.ScoreDistributionTransformer;
import org.dmg.pmml.ScoreFrequency;
import org.dmg.pmml.ScoreProbability;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.adapters.ScoreDistributionAdapter;
import org.dmg.pmml.tree.Node;
import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.visitors.AbstractVisitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

abstract
public class ScoreDistributionPolymorphismTest {

	private ScoreDistributionPolymorphismTest(){
	}

	static
	public PMML load(ScoreDistributionTransformer scoreDistributionTransormer) throws Exception {
		JAXBSerializer serializer = new JAXBSerializer();

		return load(serializer, scoreDistributionTransormer);
	}

	static
	public PMML load(JAXBSerializer serializer, ScoreDistributionTransformer scoreDistributionTransormer) throws Exception {
		ScoreDistributionTransformer defaultScoreDistributionTransformer = ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.get();

		try {
			ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.set(scoreDistributionTransormer);

			return ResourceUtil.unmarshal(serializer, ScoreDistributionPolymorphismTest.class);
		} finally {
			ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.set(defaultScoreDistributionTransformer);
		}
	}

	static
	public void checkComplex(PMML pmml){
		Set<Node> visitedNodes = new HashSet<>();
		Set<ScoreDistribution> visitedScoreDistributions = new HashSet<>();

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Node node){
				visitedNodes.add(node);

				if(node.hasScoreDistributions()){
					List<ScoreDistribution> scoreDistributions = node.getScoreDistributions();

					for(ScoreDistribution scoreDistribution : scoreDistributions){
						assertTrue(scoreDistribution instanceof ComplexScoreDistribution);
					}

					visitedScoreDistributions.addAll(scoreDistributions);
				}

				return super.visit(node);
			}
		};
		visitor.applyTo(pmml);

		assertEquals(3, visitedNodes.size());
		assertEquals(4, visitedScoreDistributions.size());
	}

	static
	public void checkSimplified(PMML pmml){
		Set<Node> visitedNodes = new HashSet<>();
		Set<ScoreDistribution> visitedScoreDistributions = new HashSet<>();

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Node node){
				Object id = node.getId();

				visitedNodes.add(node);

				if(("parent").equals(id)){
					assertFalse(node.hasScoreDistributions());
				} else

				if(("false child").equals(id)){
					List<ScoreDistribution> scoreDistributions = node.getScoreDistributions();

					for(ScoreDistribution scoreDistribution : scoreDistributions){
						assertTrue(scoreDistribution instanceof ScoreFrequency);
					}
				} else

				if(("true child").equals(id)){
					List<ScoreDistribution> scoreDistributions = node.getScoreDistributions();

					for(ScoreDistribution scoreDistribution : scoreDistributions){
						assertTrue(scoreDistribution instanceof ScoreProbability);
					}
				} else

				{
					fail();
				}

				return super.visit(node);
			}

			@Override
			public VisitorAction visit(ScoreDistribution scoreDistribution){
				visitedScoreDistributions.add(scoreDistribution);

				return super.visit(scoreDistribution);
			}
		};
		visitor.applyTo(pmml);

		assertEquals(3, visitedNodes.size());
		assertEquals(4, visitedScoreDistributions.size());
	}
}