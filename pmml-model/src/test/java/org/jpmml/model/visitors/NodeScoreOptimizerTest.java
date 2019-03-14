/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.False;
import org.dmg.pmml.MathContext;
import org.dmg.pmml.MiningFunction;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.True;
import org.dmg.pmml.tree.BranchNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.jpmml.model.VisitorBattery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class NodeScoreOptimizerTest {

	@Test
	public void parseAndIntern(){
		Node node1a = new BranchNode()
			.setScore("1")
			.setPredicate(new True());

		Node node2a = new LeafNode()
			.setScore("2")
			.setPredicate(new False());

		Node node2b = new BranchNode()
			.setScore("2.0")
			.setPredicate(new False());

		Node node2c = new LeafNode()
			.setScore(2.0f)
			.setPredicate(new True());

		node1a.addNodes(node2a, node2b, node2c);

		Node node3a = new LeafNode()
			.setScore("error")
			.setPredicate(new False());

		node2b.addNodes(node3a);

		TreeModel treeModel = new TreeModel(MiningFunction.CLASSIFICATION, new MiningSchema(), node1a)
			.setMathContext(MathContext.FLOAT);

		VisitorBattery visitorBattery = new VisitorBattery();
		visitorBattery.add(NodeScoreOptimizer.class);
		visitorBattery.add(FloatInterner.class);

		visitorBattery.applyTo(treeModel);

		assertEquals("1", node1a.getScore());

		assertEquals("2", node2a.getScore());
		assertEquals("2.0", node2b.getScore());
		assertEquals(2.0f, node2c.getScore());

		assertEquals("error", node3a.getScore());

		treeModel.setMiningFunction(MiningFunction.REGRESSION);

		visitorBattery.applyTo(treeModel);

		assertEquals(1.0f, node1a.getScore());

		assertEquals(2.0f, node2a.getScore());
		assertEquals(2.0f, node2b.getScore());
		assertEquals(2.0f, node2c.getScore());

		assertSame(node2a.getScore(), node2b.getScore());
		assertSame(node2a.getScore(), node2c.getScore());

		assertEquals("error", node3a.getScore());
	}
}