/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.dmg.pmml.mining;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SegmentationTest {

	@Test
	public void multipleModelMethod(){
		Segmentation.MultipleModelMethod[] multipleModelMethods = Segmentation.MultipleModelMethod.values();

		assertEquals(10 + 2, multipleModelMethods.length);

		checkOrder(Segmentation.MultipleModelMethod.MAJORITY_VOTE, Segmentation.MultipleModelMethod.WEIGHTED_MAJORITY_VOTE);
		checkOrder(Segmentation.MultipleModelMethod.AVERAGE, Segmentation.MultipleModelMethod.WEIGHTED_AVERAGE);
		checkOrder(Segmentation.MultipleModelMethod.MEDIAN, Segmentation.MultipleModelMethod.WEIGHTED_MEDIAN);
		checkOrder(Segmentation.MultipleModelMethod.SUM, Segmentation.MultipleModelMethod.WEIGHTED_SUM);
	}

	static
	private <E extends Enum<?>> void checkOrder(E first, E second){
		assertEquals(first.ordinal() + 1, second.ordinal());
	}
}