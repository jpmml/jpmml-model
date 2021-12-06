/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.Segment;
import org.jpmml.model.ChainedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActiveFieldFinderTest {

	@Test
	public void findChained() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(ChainedSegmentationTest.class);

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Segment segment){
				Model model = segment.getModel();

				String id = segment.getId();

				if("first".equals(id)){
					checkFields(Arrays.asList("x1_squared"), model);
				} else

				if("second".equals(id)){
					checkFields(Arrays.asList("x2", "x2_squared"), model);
				} else

				if("third".equals(id)){
					checkFields(Arrays.asList("x3"), model);
				} else

				if("sum".equals(id)){
					checkFields(Arrays.asList("first_output", "second_output", "third_output"), model);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(segment);
			}
		};

		visitor.applyTo(pmml);
	}

	static
	private void checkFields(Collection<String> names, Model model){
		assertEquals(new HashSet<>(names), ActiveFieldFinder.getFieldNames(model));
	}
}