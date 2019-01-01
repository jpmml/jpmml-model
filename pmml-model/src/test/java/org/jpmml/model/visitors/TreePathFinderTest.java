/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.MiningFunction;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TreePathFinderTest {

	@Test
	public void find(){
		Node node1a = new ComplexNode();

		Node node2a = new ComplexNode();
		Node node2b = new ComplexNode();
		Node node2c = new ComplexNode();

		node1a.addNodes(node2a, node2b, node2c);

		Node node3a = new ComplexNode();
		Node node3b = new ComplexNode();

		node2b.addNodes(node3a);
		node2c.addNodes(node3b);

		Node node4a = new ComplexNode();

		node3a.addNodes(node4a);

		TreeModel treeModel = new TreeModel(MiningFunction.CLASSIFICATION, new MiningSchema(), node1a);

		TreePathFinder finder = new TreePathFinder();
		finder.applyTo(treeModel);

		Map<Node, List<Node>> paths = finder.getPaths();

		assertEquals(3, paths.size());

		assertEquals(Arrays.asList(node1a, node2a), paths.get(node2a));
		assertEquals(Arrays.asList(node1a, node2b, node3a, node4a), paths.get(node4a));
		assertEquals(Arrays.asList(node1a, node2c, node3b), paths.get(node3b));
	}
}