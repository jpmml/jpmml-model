/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.dmg.pmml.adapters.NodeAdapter;

/**
 * <p>
 * A {@link Node} element converter (two-way transformer) between the default representation ({@link ComplexNode}) and a custom representation.
 * </p>
 *
 * @see NodeAdapter
 * @see NodeAdapter#NODE_TRANSFORMER_PROVIDER
 */
public interface NodeTransformer {

	Node fromComplexNode(ComplexNode complexNode);

	ComplexNode toComplexNode(Node node);
}