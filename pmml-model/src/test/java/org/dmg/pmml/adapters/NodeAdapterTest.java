/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import org.dmg.pmml.PMML;
import org.dmg.pmml.tree.NodeTransformer;
import org.dmg.pmml.tree.SimplifyingNodeTransformer;
import org.jpmml.model.resources.NodePolymorphismTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class NodeAdapterTest {

	@Test
	public void checkDefault(){
		NodeTransformer defaultNodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		assertSame(SimplifyingNodeTransformer.INSTANCE, defaultNodeTransformer);
	}

	@Test
	public void loadComplex() throws Exception {
		PMML pmml = NodePolymorphismTest.load(null);

		NodePolymorphismTest.checkComplex(pmml);
	}

	@Test
	public void loadSimplified() throws Exception {
		PMML pmml = NodePolymorphismTest.load(SimplifyingNodeTransformer.INSTANCE);

		NodePolymorphismTest.checkSimplified(pmml);
	}
}