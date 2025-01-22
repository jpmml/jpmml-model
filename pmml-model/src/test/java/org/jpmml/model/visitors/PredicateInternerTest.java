/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.Array;
import org.dmg.pmml.False;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.SimpleSetPredicate;
import org.dmg.pmml.True;
import org.dmg.pmml.tree.BranchNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PredicateInternerTest {

	@Test
	public void internSimplePredicate(){
		Predicate left = new SimplePredicate("x", SimplePredicate.Operator.EQUAL, "1");
		Predicate right = new SimplePredicate("x", SimplePredicate.Operator.EQUAL, "1");

		checkTree(left, right);
	}

	@Test
	public void internSimpleSetPredicate(){
		Predicate left = new SimpleSetPredicate("x", SimpleSetPredicate.BooleanOperator.IS_IN, new Array(Array.Type.STRING, "1"));
		Predicate right = new SimpleSetPredicate("x", SimpleSetPredicate.BooleanOperator.IS_IN, new Array(Array.Type.STRING, "1"));

		checkTree(left, right);
	}

	@Test
	public void internTrue(){
		checkTree(new True(), new True());
		checkTree(new True(), True.INSTANCE);
	}

	@Test
	public void internFalse(){
		checkTree(new False(), new False());
		checkTree(new False(), False.INSTANCE);
	}

	static
	private void checkTree(Predicate left, Predicate right){
		checkTree(new LeafNode(null, left), new LeafNode(null, right));
	}

	static
	private void checkTree(Node leftChild, Node rightChild){
		Node root = new BranchNode(null, True.INSTANCE)
			.addNodes(leftChild, rightChild);

		TreeModel treeModel = new TreeModel()
			.setNode(root);

		assertNotSame(leftChild.requirePredicate(), rightChild.requirePredicate());

		PredicateInterner interner = new PredicateInterner();
		interner.applyTo(treeModel);

		assertSame(leftChild.requirePredicate(), rightChild.requirePredicate());
	}
}