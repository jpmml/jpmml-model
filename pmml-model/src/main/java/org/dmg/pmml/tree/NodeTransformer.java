/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

public interface NodeTransformer {

	Node fromComplexNode(ComplexNode complexNode);

	ComplexNode toComplexNode(Node node);
}