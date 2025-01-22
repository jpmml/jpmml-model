/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.MiningFunction;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.tree.BranchNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreePathFinderTest {

	@Test
	public void find(){
		Node node1a = new BranchNode();

		Node node2a = new LeafNode();
		Node node2b = new BranchNode();
		Node node2c = new BranchNode();

		node1a.addNodes(node2a, node2b, node2c);

		Node node3a = new BranchNode();
		Node node3b = new LeafNode();

		node2b.addNodes(node3a);
		node2c.addNodes(node3b);

		Node node4a = new LeafNode();

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