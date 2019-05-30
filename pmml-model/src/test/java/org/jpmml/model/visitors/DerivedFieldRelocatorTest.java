/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;
import java.util.Set;

import org.dmg.pmml.DerivedField;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.Segment;
import org.jpmml.model.ChainedSegmentationTest;
import org.jpmml.model.FieldNameUtil;
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class DerivedFieldRelocatorTest {

	@Test
	public void relocateChained() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(ChainedSegmentationTest.class);

		TransformationDictionaryCleaner cleaner = new TransformationDictionaryCleaner();
		cleaner.applyTo(pmml);

		DerivedFieldRelocator relocator = new DerivedFieldRelocator();
		relocator.applyTo(pmml);

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Segment segment){
				Model model = segment.getModel();

				LocalTransformations localTransformations = model.getLocalTransformations();

				if(("first").equals(segment.getId())){
					checkFields(FieldNameUtil.create("x1_squared"), localTransformations.getDerivedFields());
				} else

				if(("second").equals(segment.getId())){
					checkFields(FieldNameUtil.create("x2_squared"), localTransformations.getDerivedFields());
				} else

				if(("third").equals(segment.getId())){
					assertNull(localTransformations);
				} else

				if(("sum").equals(segment.getId())){
					assertNull(localTransformations);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(segment);
			}

			@Override
			public VisitorAction visit(TransformationDictionary transformationDictionary){
				assertFalse(transformationDictionary.hasDerivedFields());

				return super.visit(pmml);
			}
		};
		visitor.applyTo(pmml);
	}

	@Test
	public void relocateNested() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(NestedSegmentationTest.class);

		DerivedFieldRelocator relocator = new DerivedFieldRelocator();
		relocator.applyTo(pmml);

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Segment segment){
				Model model = segment.getModel();

				LocalTransformations localTransformations = model.getLocalTransformations();

				if(("first").equals(segment.getId())){
					assertFalse(localTransformations.hasDerivedFields());
				} else

				if(("second").equals(segment.getId())){
					checkFields(FieldNameUtil.create("x12", "x123", "x1234", "x12345"), localTransformations.getDerivedFields());
				} else

				if(("third").equals(segment.getId())){
					assertFalse(localTransformations.hasDerivedFields());
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
	private void checkFields(Set<FieldName> names, Collection<DerivedField> fields){
		assertEquals(names, FieldUtil.nameSet(fields));
	}
}