/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.MiningModel;
import org.dmg.pmml.mining.Segment;
import org.dmg.pmml.regression.RegressionModel;
import org.jpmml.model.ChainedSegmentationTest;
import org.jpmml.model.FieldNameUtil;
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.PMMLUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiningSchemaCleanerTest {

	@Test
	public void cleanChained() throws Exception {
		PMML pmml = PMMLUtil.loadResource(ChainedSegmentationTest.class);

		MiningSchemaCleaner cleaner = new MiningSchemaCleaner();
		cleaner.applyTo(pmml);

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(MiningModel miningModel){
				MiningSchema miningSchema = miningModel.getMiningSchema();

				checkMiningSchema(FieldNameUtil.create("y", "x1", "x2", "x3"), miningSchema);

				return super.visit(miningModel);
			}

			@Override
			public VisitorAction visit(RegressionModel regressionModel){
				MiningSchema miningSchema = regressionModel.getMiningSchema();

				Segment segment = (Segment)VisitorUtil.getParent(this);

				String id = segment.getId();

				if("first".equals(id)){
					checkMiningSchema(FieldNameUtil.create("x1"), miningSchema);
				} else

				if("second".equals(id)){
					checkMiningSchema(FieldNameUtil.create("x2"), miningSchema);
				} else

				if("third".equals(id)){
					checkMiningSchema(FieldNameUtil.create("x3"), miningSchema);
				} else

				if("sum".equals(id)){
					checkMiningSchema(FieldNameUtil.create("y", "first_output", "second_output", "third_output"), miningSchema);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(regressionModel);
			}
		};

		visitor.applyTo(pmml);
	}

	@Test
	public void cleanNested() throws Exception {
		PMML pmml = PMMLUtil.loadResource(NestedSegmentationTest.class);

		MiningSchemaCleaner cleaner = new MiningSchemaCleaner();
		cleaner.applyTo(pmml);

		Visitor miningModelVisitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(MiningModel miningModel){
				MiningSchema miningSchema = miningModel.getMiningSchema();

				String id;

				try {
					Segment segment = (Segment)VisitorUtil.getParent(this);

					id = segment.getId();
				} catch(ClassCastException cce){
					id = null;
				} // End try

				if(id == null){
					checkMiningSchema(FieldNameUtil.create("x1", "x2", "x3", "x4", "x5"), miningSchema);
				} else

				if("first".equals(id)){
					checkMiningSchema(FieldNameUtil.create("x12", "x3", "x4", "x5"), miningSchema);
				} else

				if("second".equals(id)){
					checkMiningSchema(FieldNameUtil.create("x123", "x12345"), miningSchema);
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
				MiningSchema miningSchema = regressionModel.getMiningSchema();

				checkMiningSchema(FieldNameUtil.create("x123"), miningSchema);

				return super.visit(regressionModel);
			}
		};

		regressionModelVisitor.applyTo(pmml);
	}

	static
	private void checkMiningSchema(Set<FieldName> names, MiningSchema miningSchema){
		assertEquals(names, getFieldNames(miningSchema));
	}

	static
	private Set<FieldName> getFieldNames(MiningSchema miningSchema){
		Set<FieldName> result = new LinkedHashSet<>();

		List<MiningField> miningFields = miningSchema.getMiningFields();
		for(MiningField miningField : miningFields){
			result.add(miningField.getName());
		}

		return result;
	}
}