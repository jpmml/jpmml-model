/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.ComplexScoreDistribution;
import org.dmg.pmml.ScoreDistribution;
import org.dmg.pmml.True;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class ScoreDistributionInternerTest {

	@Test
	public void intern(){
		ScoreDistribution left = new ComplexScoreDistribution("event")
			.setProbability(0.33d);

		ScoreDistribution right = new ComplexScoreDistribution("event")
			.setProbability(0.33d);

		Node leftChild = createNode(left);
		Node rightChild = createNode(right);

		Node root = new ComplexNode(null, True.INSTANCE)
			.addNodes(leftChild, rightChild);

		TreeModel treeModel = new TreeModel()
			.setNode(root);

		for(int i = 0; i < 2; i++){
			assertNotSame((leftChild.getScoreDistributions()).get(i), (rightChild.getScoreDistributions()).get(i));
		}

		ScoreDistributionInterner interner = new ScoreDistributionInterner();
		interner.applyTo(treeModel);

		for(int i = 0; i < 2; i++){
			assertSame((leftChild.getScoreDistributions()).get(i), (rightChild.getScoreDistributions()).get(i));
		}
	}

	static
	private Node createNode(ScoreDistribution event){
		ScoreDistribution noEvent = new ComplexScoreDistribution("no-event")
			.setProbability(1d - (event.requireProbability()).doubleValue());

		Node node = new ComplexNode()
			.addScoreDistributions(event, noEvent);

		return node;
	}
}