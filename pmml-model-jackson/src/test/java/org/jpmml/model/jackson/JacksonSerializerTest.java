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
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.Serializer;
import org.jpmml.model.SerializerTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class JacksonSerializerTest extends SerializerTest {

	@Test
	public void jsonCloneFragment() throws Exception {
		Serializer serializer = new JacksonSerializer(JacksonUtil.createObjectMapper(null), Apply.class);

		FieldRef left = new FieldRef("x");
		FieldRef right = new FieldRef("x");

		assertSame(left.getField(), right.getField());

		Apply apply = new Apply(PMMLFunctions.ADD)
			.addExpressions(left, right);

		Apply clonedApply = checkedClone(serializer, apply);

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
		Serializer serializer = new JacksonSerializer();

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

		checkedClone(serializer, pmml);
	}
}