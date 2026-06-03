/*
 * Copyright (c) 2026 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.dmg.pmml.adapters.NumberUtil;
import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.Serializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ScoreTest {

	@Test
	public void jaxbClone() throws Exception {
		Serializer serializer = new JAXBSerializer();

		Score score = new Score()
			.setTargetField("decision")
			.setValue(1d);

		Score jaxbScore = SerializationUtil.clone(serializer, score);

		assertEquals(score.getTargetField(), jaxbScore.getTargetField());
		assertNotEquals(score.getValue(), jaxbScore.getValue());
		assertEquals(NumberUtil.printNumber((Number)score.getValue()), jaxbScore.getValue());
	}
}