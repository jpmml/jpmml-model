/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.ArrayList;

import org.dmg.pmml.MiningFunction;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ArrayListTransformerTest {

	@Test
	public void transform(){
		Node node1a = new ComplexNode();

		Node node2a = new ComplexNode();
		Node node2b = new ComplexNode();

		node1a.addNodes(node2a, node2b);

		Node node3a = new ComplexNode();

		node2a.addNodes(node3a);

		assertTrue(node1a.getNodes() instanceof ArrayList);
		assertTrue(node2a.getNodes() instanceof ArrayList);

		TreeModel treeModel = new TreeModel(MiningFunction.CLASSIFICATION, new MiningSchema(), node1a);

		ArrayListTransformer transformer = new ArrayListTransformer();
		transformer.applyTo(treeModel);

		assertTrue(node1a.getNodes() instanceof DoubletonList);
		assertTrue(node2a.getNodes() instanceof SingletonList);
	}
}