/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import java.io.InputStream;

import org.dmg.pmml.PMML;
import org.dmg.pmml.tree.NodeTransformer;
import org.dmg.pmml.tree.SimplifyingNodeTransformer;
import org.jpmml.model.PMMLUtil;
import org.jpmml.model.resources.NodePolymorphismTest;
import org.jpmml.model.resources.ResourceUtil;
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
		PMML pmml = load(null);

		NodePolymorphismTest.checkComplex(pmml);
	}

	@Test
	public void loadSimplified() throws Exception {
		PMML pmml = load(SimplifyingNodeTransformer.INSTANCE);

		NodePolymorphismTest.checkSimplified(pmml);
	}

	static
	private PMML load(NodeTransformer nodeTransformer) throws Exception {
		NodeTransformer defaultNodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		try(InputStream is = ResourceUtil.getStream(NodePolymorphismTest.class)){
			NodeAdapter.NODE_TRANSFORMER_PROVIDER.set(nodeTransformer);

			return PMMLUtil.unmarshal(is);
		} finally {
			NodeAdapter.NODE_TRANSFORMER_PROVIDER.set(defaultNodeTransformer);
		}
	}
}