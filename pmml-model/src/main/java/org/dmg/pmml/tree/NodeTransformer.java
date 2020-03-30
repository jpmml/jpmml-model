/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

public interface NodeTransformer {

	Node fromComplexNode(ComplexNode complexNode);

	default
	ComplexNode toComplexNode(Node node){
		return node.toComplexNode();
	}
}