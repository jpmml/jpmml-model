/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;
import java.util.ListIterator;

import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.DecisionTree;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;

abstract
public class NodeFilterer extends AbstractVisitor {

	abstract
	public Node filter(Node node);

	public void filterAll(List<Node> nodes){

		for(ListIterator<Node> it = nodes.listIterator(); it.hasNext(); ){
			it.set(filter(it.next()));
		}
	}

	@Override
	public VisitorAction visit(DecisionTree decisionTree){
		decisionTree.setNode(filter(decisionTree.getNode()));

		return super.visit(decisionTree.getNode());
	}

	@Override
	public VisitorAction visit(ComplexNode complexNode){
		return super.visit(complexNode);
	}

	@Override
	public VisitorAction visit(Node node){

		if(node.hasNodes()){
			filterAll(node.getNodes());
		}

		return super.visit(node);
	}

	@Override
	public VisitorAction visit(TreeModel treeModel){
		treeModel.setNode(filter(treeModel.getNode()));

		return super.visit(treeModel);
	}
}