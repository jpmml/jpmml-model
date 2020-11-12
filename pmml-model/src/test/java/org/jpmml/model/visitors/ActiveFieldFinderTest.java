/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Set;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.Segment;
import org.jpmml.model.ChainedSegmentationTest;
import org.jpmml.model.FieldNameUtil;
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
					checkFields(FieldNameUtil.create("x1_squared"), model);
				} else

				if("second".equals(id)){
					checkFields(FieldNameUtil.create("x2", "x2_squared"), model);
				} else

				if("third".equals(id)){
					checkFields(FieldNameUtil.create("x3"), model);
				} else

				if("sum".equals(id)){
					checkFields(FieldNameUtil.create("first_output", "second_output", "third_output"), model);
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
	private void checkFields(Set<FieldName> names, Model model){
		assertEquals(names, ActiveFieldFinder.getFieldNames(model));
	}
}