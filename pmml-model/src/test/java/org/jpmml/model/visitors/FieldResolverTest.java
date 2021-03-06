/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.dmg.pmml.Apply;
import org.dmg.pmml.DerivedField;
import org.dmg.pmml.Field;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.Model;
import org.dmg.pmml.Output;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLFunctions;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.Segment;
import org.dmg.pmml.mining.VariableWeight;
import org.dmg.pmml.mining.WeightedSegmentationTest;
import org.dmg.pmml.regression.RegressionTable;
import org.jpmml.model.ChainedSegmentationTest;
import org.jpmml.model.FieldNameUtil;
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.filters.ImportFilter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldResolverTest {

	@Test
	public void resolveChained() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(ChainedSegmentationTest.class);

		Set<FieldName> dataFieldNames = FieldNameUtil.create("y", "x1", "x2", "x3", "x4");

		Set<FieldName> pmmlNames = FieldNameUtil.create(dataFieldNames, "x1_squared", "x1_cubed");

		FieldResolver applyResolver = new FieldResolver(){

			@Override
			public VisitorAction visit(Apply apply){
				Collection<Field<?>> fields = getFields();

				String function = apply.getFunction();

				if((PMMLFunctions.MULTIPLY).equals(function)){
					DerivedField derivedField = (DerivedField)getParent();

					FieldName name = derivedField.getName();

					if("x1_squared".equals(name.getValue())){
						checkFields(dataFieldNames, fields);
					} else

					if("x1_cubed".equals(name.getValue())){
						checkFields(FieldNameUtil.create(dataFieldNames, "x1_squared"), fields);
					} else

					{
						throw new AssertionError();
					}
				} else

				if((PMMLFunctions.POW).equals(function)){
					checkFields(FieldNameUtil.create("x"), fields);
				} else

				if("square".equals(function)){
					checkFields(FieldNameUtil.create(pmmlNames, "first_output"), fields);
				} else

				if("cube".equals(function)){
					checkFields(FieldNameUtil.create(pmmlNames, "first_output", "x2_squared"), fields);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(apply);
			}
		};

		applyResolver.applyTo(pmml);

		checkFields(Collections.emptySet(), applyResolver.getFields());

		FieldResolver regressionTableResolver = new FieldResolver(){

			@Override
			public VisitorAction visit(RegressionTable regressionTable){
				Collection<Field<?>> fields = getFields();

				Segment segment = (Segment)getParent(1);

				String id = segment.getId();

				if("first".equals(id)){
					checkFields(pmmlNames, fields);
				} else

				if("second".equals(id)){
					checkFields(FieldNameUtil.create(pmmlNames, "first_output", "x2_squared", "x2_cubed"), fields);
				} else

				if("third".equals(id)){
					checkFields(FieldNameUtil.create(pmmlNames, "first_output", "second_output"), fields);
				} else

				if("sum".equals(id)){
					checkFields(FieldNameUtil.create(pmmlNames, "first_output", "second_output", "third_output"), fields);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(regressionTable);
			}
		};

		regressionTableResolver.applyTo(pmml);

		checkFields(Collections.emptySet(), regressionTableResolver.getFields());

		FieldResolver predicateResolver = new FieldResolver(){

			@Override
			public VisitorAction visit(SimplePredicate simplePredicate){
				Collection<Field<?>> fields = getFields();

				Segment segment = (Segment)getParent();

				String id = segment.getId();

				if("first".equals(id)){
					checkFields(pmmlNames, fields);
				} else

				if("second".equals(id)){
					checkFields(FieldNameUtil.create(pmmlNames, "first_output"), fields);
				} else

				if("third".equals(id)){
					checkFields(FieldNameUtil.create(pmmlNames, "first_output", "second_output"), fields);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(simplePredicate);
			}
		};

		predicateResolver.applyTo(pmml);

		checkFields(Collections.emptySet(), predicateResolver.getFields());
	}

	@Test
	public void resolveNested() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(NestedSegmentationTest.class);

		Set<FieldName> dataFieldNames = FieldNameUtil.create("y", "x1", "x2", "x3", "x4", "x5");

		FieldResolver applyResolver = new FieldResolver(){

			@Override
			public VisitorAction visit(Apply apply){
				Collection<Field<?>> fields = getFields();

				DerivedField derivedField = (DerivedField)getParent();

				FieldName name = derivedField.getName();

				if("x12".equals(name.getValue())){
					checkFields(dataFieldNames, fields);
				} else

				if("x123".equals(name.getValue())){
					checkFields(FieldNameUtil.create(dataFieldNames, "x12"), fields);
				} else

				if("x1234".equals(name.getValue())){
					checkFields(FieldNameUtil.create(dataFieldNames, "x12", "x123"), fields);
				} else

				if("x12345".equals(name.getValue())){
					checkFields(FieldNameUtil.create(dataFieldNames, "x12", "x123", "x1234"), fields);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(apply);
			}
		};

		applyResolver.applyTo(pmml);

		FieldResolver regressionTableResolver = new FieldResolver(){

			@Override
			public VisitorAction visit(RegressionTable regressionTable){
				Collection<Field<?>> fields = getFields();

				checkFields(FieldNameUtil.create(dataFieldNames, "x12", "x123", "x1234", "x12345"), fields);

				return super.visit(regressionTable);
			}
		};

		regressionTableResolver.applyTo(pmml);
	}

	@Test
	public void resolveWeighted() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(WeightedSegmentationTest.class, new ImportFilter());

		Set<FieldName> dataFieldNames = FieldNameUtil.create("y", "x1", "x2");

		FieldResolver regressionTableResolver = new FieldResolver(){

			@Override
			public VisitorAction visit(RegressionTable regressionTable){
				Collection<Field<?>> fields = getFields();

				Segment segment = (Segment)getParent(1);

				String id = segment.getId();

				if("first".equals(id)){
					checkFields(dataFieldNames, fields);
				} else

				if("second".equals(id)){
					checkFields(FieldNameUtil.create(dataFieldNames, "x1_squared"), fields);
				} else

				if("third".equals(id)){
					checkFields(dataFieldNames, fields);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(regressionTable);
			}
		};

		regressionTableResolver.applyTo(pmml);

		FieldResolver variableWeightResolver = new FieldResolver(){

			@Override
			public VisitorAction visit(VariableWeight variableWeight){
				Collection<Field<?>> fields;

				Segment segment = (Segment)getParent();

				Model model = segment.getModel();

				Output output = model.getOutput();
				if(output != null && output.hasOutputFields()){
					fields = getFields(output);
				} else

				{
					fields = getFields();
				}

				String id = segment.getId();

				if("first".equals(id)){
					checkFields(dataFieldNames, fields);
				} else

				if("second".equals(id)){
					checkFields(FieldNameUtil.create(dataFieldNames, "second_output"), fields);
				} else

				if("third".equals(id)){
					checkFields(FieldNameUtil.create(dataFieldNames, "third_output"), fields);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(variableWeight);
			}
		};

		variableWeightResolver.applyTo(pmml);
	}

	static
	private void checkFields(Set<FieldName> names, Collection<Field<?>> fields){
		Set<FieldName> fieldNames = fields.stream()
			.map(field -> field.getName())
			.collect(Collectors.toSet());

		assertEquals(names, fieldNames);
	}
}