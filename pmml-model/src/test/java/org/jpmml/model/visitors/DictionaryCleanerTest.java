/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;
import java.util.Set;

import org.dmg.pmml.DerivedField;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.PMML;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ChainedSegmentationTest;
import org.jpmml.model.FieldNameUtil;
import org.jpmml.model.FieldUtil;
import org.jpmml.model.PMMLUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DictionaryCleanerTest {

	@Test
	public void cleanChained() throws Exception {
		PMML pmml = PMMLUtil.loadResource(ChainedSegmentationTest.class);

		DictionaryCleaner cleaner = new DictionaryCleaner();
		cleaner.applyTo(pmml);

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(LocalTransformations localTransformations){
				checkFields(FieldNameUtil.create("x2_squared"), localTransformations.getDerivedFields());

				return super.visit(localTransformations);
			}

			@Override
			public VisitorAction visit(TransformationDictionary transformationDictionary){
				checkFields(FieldNameUtil.create("x1_squared"), transformationDictionary.getDerivedFields());

				return super.visit(transformationDictionary);
			}
		};

		visitor.applyTo(pmml);
	}

	static
	private void checkFields(Set<FieldName> names, Collection<DerivedField> derivedFields){
		assertEquals(names, FieldUtil.nameSet(derivedFields));
	}
}