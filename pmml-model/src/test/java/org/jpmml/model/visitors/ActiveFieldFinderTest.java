/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.Segment;
import org.jpmml.model.resources.ChainedSegmentationTest;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ActiveFieldFinderTest {

	@Test
	public void findChained() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(ChainedSegmentationTest.class);

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Segment segment){
				Model model = segment.requireModel();

				String id = segment.getId();

				if(Objects.equals("first", id)){
					checkFields(Arrays.asList("x1_squared"), model);
				} else

				if(Objects.equals("second", id)){
					checkFields(Arrays.asList("x2", "x2_squared"), model);
				} else

				if(Objects.equals("third", id)){
					checkFields(Arrays.asList("x3"), model);
				} else

				if(Objects.equals("sum", id)){
					checkFields(Arrays.asList("first_output", "second_output", "third_output"), model);
				} else

				{
					fail();
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