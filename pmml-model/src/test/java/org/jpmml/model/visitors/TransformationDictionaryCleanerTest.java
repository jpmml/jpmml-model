/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.DerivedField;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.MiningModel;
import org.dmg.pmml.mining.Segment;
import org.dmg.pmml.regression.RegressionModel;
import org.jpmml.model.ChainedSegmentationTest;
import org.jpmml.model.FieldNameUtil;
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TransformationDictionaryCleanerTest {

	@Test
	public void cleanChained() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(ChainedSegmentationTest.class);

		TransformationDictionaryCleaner cleaner = new TransformationDictionaryCleaner();
		cleaner.applyTo(pmml);

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(RegressionModel regressionModel){
				LocalTransformations localTransformations = regressionModel.getLocalTransformations();

				Segment segment = (Segment)getParent();

				String id = segment.getId();

				if("first".equals(id)){
					assertNull(localTransformations);
				} else

				if("second".equals(id)){
					checkFields(FieldNameUtil.create("x2_squared"), localTransformations.getDerivedFields());
				} else

				if("third".equals(id)){
					assertNull(localTransformations);
				} else

				if("sum".equals(id)){
					assertNull(localTransformations);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(regressionModel);
			}

			@Override
			public VisitorAction visit(TransformationDictionary transformationDictionary){
				checkFields(FieldNameUtil.create("x1_squared"), transformationDictionary.getDerivedFields());

				return super.visit(transformationDictionary);
			}
		};

		visitor.applyTo(pmml);

		TransformationDictionary transformationDictionary = pmml.getTransformationDictionary();

		assertTrue(transformationDictionary.hasDerivedFields());

		List<Model> models = pmml.getModels();
		models.clear();

		cleaner.applyTo(pmml);

		assertFalse(transformationDictionary.hasDerivedFields());
	}

	@Test
	public void cleanNested() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(NestedSegmentationTest.class);

		assertNotNull(pmml.getTransformationDictionary());

		TransformationDictionaryCleaner cleaner = new TransformationDictionaryCleaner();
		cleaner.applyTo(pmml);

		assertNull(pmml.getTransformationDictionary());

		Visitor miningModelVisitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(MiningModel miningModel){
				LocalTransformations localTransformations = miningModel.getLocalTransformations();

				String id;

				try {
					Segment segment = (Segment)getParent();

					id = segment.getId();
				} catch(ClassCastException cce){
					id = null;
				} // End try

				if(id == null){
					checkFields(FieldNameUtil.create("x12"), localTransformations.getDerivedFields());
				} else

				if("first".equals(id)){
					checkFields(FieldNameUtil.create("x123", "x1234", "x12345"), localTransformations.getDerivedFields());
				} else

				if("second".equals(id)){
					assertNull(localTransformations);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(miningModel);
			}
		};

		miningModelVisitor.applyTo(pmml);

		Visitor regressionModelVisitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(RegressionModel regressionModel){
				LocalTransformations localTransformations = regressionModel.getLocalTransformations();

				assertNull(localTransformations);

				return super.visit(regressionModel);
			}
		};

		regressionModelVisitor.applyTo(pmml);
	}

	static
	private void checkFields(Set<FieldName> names, Collection<DerivedField> derivedFields){
		assertEquals(names, FieldUtil.nameSet(derivedFields));
	}
}