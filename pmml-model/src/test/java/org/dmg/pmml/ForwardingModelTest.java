/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.jpmml.model.visitors.AbstractVisitor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForwardingModelTest {

	@Test
	public void visit(){
		Node root = new LeafNode(1d, True.INSTANCE);

		TreeModel treeModel = new TreeModel(MiningFunction.REGRESSION, new MiningSchema(), root);

		ForwardingModel forwardingModel = new ForwardingModel(treeModel);

		Set<Model> visitedModels = new HashSet<>();
		Set<Node> visitedNodes = new HashSet<>();

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Model model){
				visitedModels.add(model);

				return super.visit(model);
			}

			@Override
			public VisitorAction visit(Node node){
				visitedNodes.add(node);

				return super.visit(node);
			}
		};

		visitor.applyTo(forwardingModel);

		assertEquals(Collections.singleton(treeModel), visitedModels);
		assertEquals(Collections.singleton(root), visitedNodes);

		forwardingModel = new ForwardingModel(treeModel){

			@Override
			public VisitorAction accept(Visitor visitor){
				VisitorAction status = visitor.visit(this);

				return VisitorAction.SKIP;
			}
		};

		visitedModels.clear();
		visitedNodes.clear();

		visitor.applyTo(forwardingModel);

		assertEquals(Collections.singleton(forwardingModel), visitedModels);
		assertEquals(Collections.emptySet(), visitedNodes);
	}
}