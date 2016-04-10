/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.ArrayList;

import org.dmg.pmml.MiningFunctionType;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Node;
import org.dmg.pmml.TreeModel;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ArrayListTransformerTest {

	@Test
	public void transform(){
		Node node1a = new Node();

		Node node2a = new Node();
		Node node2b = new Node();

		node1a.addNodes(node2a, node2b);

		Node node3a = new Node();

		node2a.addNodes(node3a);

		assertTrue(node1a.getNodes() instanceof ArrayList);
		assertTrue(node2a.getNodes() instanceof ArrayList);

		TreeModel treeModel = new TreeModel(MiningFunctionType.CLASSIFICATION, new MiningSchema(), node1a);

		ArrayListTransformer transformer = new ArrayListTransformer();
		transformer.applyTo(treeModel);

		assertTrue(node1a.getNodes() instanceof DoubletonList);
		assertTrue(node2a.getNodes() instanceof SingletonList);
	}
}