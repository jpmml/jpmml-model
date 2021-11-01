/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.dmg.pmml.PMMLObject;

public interface HasNode<E extends PMMLObject & HasNode<E>> {

	TreeModel.MissingValueStrategy getMissingValueStrategy();

	E setMissingValueStrategy(TreeModel.MissingValueStrategy missingValueStrategy);

	Number getMissingValuePenalty();

	E setMissingValuePenalty(Number missingValuePenalty);

	TreeModel.NoTrueChildStrategy getNoTrueChildStrategy();

	E setNoTrueChildStrategy(TreeModel.NoTrueChildStrategy noTrueChildStrategy);

	TreeModel.SplitCharacteristic getSplitCharacteristic();

	E setSplitCharacteristic(TreeModel.SplitCharacteristic splitCharacteristic);

	Node getNode();

	E setNode(Node node);
}