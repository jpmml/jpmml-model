/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.dmg.pmml.Apply;
import org.dmg.pmml.DerivedField;
import org.dmg.pmml.Field;
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
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.filters.ImportFilter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldResolverTest {

	@Test
	public void resolveChained() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(ChainedSegmentationTest.class);

		Collection<String> dataFieldNames = Arrays.asList("y", "x1", "x2", "x3", "x4");

		Collection<String> pmmlNames = join(dataFieldNames, "x1_squared", "x1_cubed");

		FieldResolver applyResolver = new FieldResolver(){

			@Override
			public VisitorAction visit(Apply apply){
				Collection<Field<?>> fields = getFields();

				String function = apply.getFunction();

				if((PMMLFunctions.MULTIPLY).equals(function)){
					DerivedField derivedField = (DerivedField)getParent();

					String name = derivedField.getName();

					if("x1_squared".equals(name)){
						checkFields(dataFieldNames, fields);
					} else

					if("x1_cubed".equals(name)){
						checkFields(join(dataFieldNames, "x1_squared"), fields);
					} else

					{
						throw new AssertionError();
					}
				} else

				if((PMMLFunctions.POW).equals(function)){
					checkFields(Arrays.asList("x"), fields);
				} else

				if("square".equals(function)){
					checkFields(join(pmmlNames, "first_output"), fields);
				} else

				if("cube".equals(function)){
					checkFields(join(pmmlNames, "first_output", "x2_squared"), fields);
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
					checkFields(join(pmmlNames, "first_output", "x2_squared", "x2_cubed"), fields);
				} else

				if("third".equals(id)){
					checkFields(join(pmmlNames, "first_output", "second_output"), fields);
				} else

				if("sum".equals(id)){
					checkFields(join(pmmlNames, "first_output", "second_output", "third_output"), fields);
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
					checkFields(join(pmmlNames, "first_output"), fields);
				} else

				if("third".equals(id)){
					checkFields(join(pmmlNames, "first_output", "second_output"), fields);
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

		Collection<String> dataFieldNames = Arrays.asList("y", "x1", "x2", "x3", "x4", "x5");

		FieldResolver applyResolver = new FieldResolver(){

			@Override
			public VisitorAction visit(Apply apply){
				Collection<Field<?>> fields = getFields();

				DerivedField derivedField = (DerivedField)getParent();

				String name = derivedField.getName();

				if("x12".equals(name)){
					checkFields(dataFieldNames, fields);
				} else

				if("x123".equals(name)){
					checkFields(join(dataFieldNames, "x12"), fields);
				} else

				if("x1234".equals(name)){
					checkFields(join(dataFieldNames, "x12", "x123"), fields);
				} else

				if("x12345".equals(name)){
					checkFields(join(dataFieldNames, "x12", "x123", "x1234"), fields);
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

				checkFields(join(dataFieldNames, "x12", "x123", "x1234", "x12345"), fields);

				return super.visit(regressionTable);
			}
		};

		regressionTableResolver.applyTo(pmml);
	}

	@Test
	public void resolveWeighted() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(WeightedSegmentationTest.class, new ImportFilter());

		Collection<String> dataFieldNames = Arrays.asList("y", "x1", "x2");

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
					checkFields(join(dataFieldNames, "x1_squared"), fields);
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
					checkFields(join(dataFieldNames, "second_output"), fields);
				} else

				if("third".equals(id)){
					checkFields(join(dataFieldNames, "third_output"), fields);
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
	private void checkFields(Collection<String> names, Collection<Field<?>> fields){
		Set<String> fieldNames = fields.stream()
			.map(field -> field.getName())
			.collect(Collectors.toSet());

		assertEquals(new HashSet<>(names), fieldNames);
	}

	static
	private <E> Collection<E> join(Collection<E> collection, E... elements){
		List<E> result = new ArrayList<>(collection);
		result.addAll(Arrays.asList(elements));

		return result;
	}
}