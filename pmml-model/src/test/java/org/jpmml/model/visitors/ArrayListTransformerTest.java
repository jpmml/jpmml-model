/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.ArrayList;
import java.util.Arrays;

import org.dmg.pmml.Array;
import org.dmg.pmml.ComplexArray;
import org.dmg.pmml.ComplexValue;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.MiningFunction;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.SimpleSetPredicate;
import org.dmg.pmml.tree.BranchNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ArrayListTransformerTest {

	@Test
	public void transform(){
		Node node1a = new BranchNode();

		Node node2a = new BranchNode();
		Node node2b = new LeafNode();

		node1a.addNodes(node2a, node2b);

		Array array = new ComplexArray()
			.setType(Array.Type.INT)
			.setValue(Arrays.asList(-1, 1));

		Predicate predicate = new SimpleSetPredicate(FieldName.create("x"), SimpleSetPredicate.BooleanOperator.IS_IN, array);

		Node node3a = new LeafNode(null, predicate);

		node2a.addNodes(node3a);

		assertTrue(node1a.getNodes() instanceof ArrayList);
		assertTrue(node2a.getNodes() instanceof ArrayList);

		Object value = array.getValue();

		assertTrue(value instanceof ArrayList);
		assertTrue(value instanceof ComplexValue);

		TreeModel treeModel = new TreeModel(MiningFunction.CLASSIFICATION, new MiningSchema(), node1a);

		ArrayListTransformer transformer = new ArrayListTransformer();
		transformer.applyTo(treeModel);

		assertTrue(node1a.getNodes() instanceof DoubletonList);
		assertTrue(node2a.getNodes() instanceof SingletonList);

		value = array.getValue();

		assertTrue(value instanceof ArrayList);
		assertTrue(value instanceof ComplexValue);
	}
}