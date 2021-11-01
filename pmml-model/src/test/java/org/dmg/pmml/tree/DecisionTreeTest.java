/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.dmg.pmml.ReflectionTest;
import org.junit.Test;

public class DecisionTreeTest extends ReflectionTest {

	@Test
	public void missingValueStrategyType() throws NoSuchFieldException {
		checkField(TreeModel.class, DecisionTree.class, "missingValueStrategy");
	}

	@Test
	public void noTrueChildStrategyType() throws NoSuchFieldException {
		checkField(TreeModel.class, DecisionTree.class, "noTrueChildStrategy");
	}

	@Test
	public void splitCharacteristicType() throws NoSuchFieldException {
		checkField(TreeModel.class, DecisionTree.class, "splitCharacteristic");
	}
}