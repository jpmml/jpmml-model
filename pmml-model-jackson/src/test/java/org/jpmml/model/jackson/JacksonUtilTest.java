/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.jackson;

import java.util.List;

import org.dmg.pmml.Apply;
import org.dmg.pmml.Expression;
import org.dmg.pmml.FieldRef;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLFunctions;
import org.dmg.pmml.ScoreDistributionTransformer;
import org.dmg.pmml.adapters.NodeAdapter;
import org.dmg.pmml.adapters.NodeAdapterTest;
import org.dmg.pmml.adapters.ScoreDistributionAdapter;
import org.dmg.pmml.tree.NodeTransformer;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.ResourceUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JacksonUtilTest {

	@Test
	public void jsonCloneFragment() throws Exception {
		FieldRef left = new FieldRef("x");
		FieldRef right = new FieldRef("x");

		assertSame(left.getField(), right.getField());

		Apply apply = new Apply(PMMLFunctions.ADD)
			.addExpressions(left, right);

		Apply clonedApply = JacksonUtil.clone(apply);

		List<Expression> clonedExpressions = clonedApply.getExpressions();

		assertEquals(2, clonedExpressions.size());

		FieldRef clonedLeft = (FieldRef)clonedExpressions.get(0);
		FieldRef clonedRight = (FieldRef)clonedExpressions.get(1);

		// XXX
		assertEquals(clonedLeft.getField(), clonedRight.getField());
		assertNotSame(clonedLeft.getField(), clonedRight.getField());
	}

	@Test
	public void jsonClone() throws Exception {
		PMML pmml;

		NodeTransformer defaultNodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();
		ScoreDistributionTransformer defaultScoreDistributionTransformer = ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.get();

		try {
			NodeAdapter.NODE_TRANSFORMER_PROVIDER.set(null);
			ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.set(null);

			pmml = ResourceUtil.unmarshal(NodeAdapterTest.class);
		} finally {
			NodeAdapter.NODE_TRANSFORMER_PROVIDER.set(defaultNodeTransformer);
			ScoreDistributionAdapter.SCOREDISTRIBUTION_TRANSFORMER_PROVIDER.set(defaultScoreDistributionTransformer);
		}

		PMML clonedPmml = JacksonUtil.clone(pmml);

		assertTrue(ReflectionUtil.equals(pmml, clonedPmml));
	}
}