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
import java.util.Objects;
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
import org.jpmml.model.filters.ImportFilter;
import org.jpmml.model.resources.ChainedSegmentationTest;
import org.jpmml.model.resources.NestedSegmentationTest;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

				String function = apply.requireFunction();

				if(Objects.equals(PMMLFunctions.MULTIPLY, function)){
					DerivedField derivedField = (DerivedField)getParent();

					String name = derivedField.requireName();

					if(Objects.equals("x1_squared", name)){
						checkFields(dataFieldNames, fields);
					} else

					if(Objects.equals("x1_cubed", name)){
						checkFields(join(dataFieldNames, "x1_squared"), fields);
					} else

					{
						fail();
					}
				} else

				if(Objects.equals(PMMLFunctions.POW, function)){
					checkFields(Arrays.asList("x"), fields);
				} else

				if(Objects.equals("square", function)){
					checkFields(join(pmmlNames, "first_output"), fields);
				} else

				if(Objects.equals("cube", function)){
					checkFields(join(pmmlNames, "first_output", "x2_squared"), fields);
				} else

				{
					fail();
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

				if(Objects.equals("first", id)){
					checkFields(pmmlNames, fields);
				} else

				if(Objects.equals("second", id)){
					checkFields(join(pmmlNames, "first_output", "x2_squared", "x2_cubed"), fields);
				} else

				if(Objects.equals("third", id)){
					checkFields(join(pmmlNames, "first_output", "second_output"), fields);
				} else

				if(Objects.equals("sum", id)){
					checkFields(join(pmmlNames, "first_output", "second_output", "third_output"), fields);
				} else

				{
					fail();
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

				if(Objects.equals("first", id)){
					checkFields(pmmlNames, fields);
				} else

				if(Objects.equals("second", id)){
					checkFields(join(pmmlNames, "first_output"), fields);
				} else

				if(Objects.equals("third", id)){
					checkFields(join(pmmlNames, "first_output", "second_output"), fields);
				} else

				{
					fail();
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

				String name = derivedField.requireName();

				if(Objects.equals("x12", name)){
					checkFields(dataFieldNames, fields);
				} else

				if(Objects.equals("x123", name)){
					checkFields(join(dataFieldNames, "x12"), fields);
				} else

				if(Objects.equals("x1234", name)){
					checkFields(join(dataFieldNames, "x12", "x123"), fields);
				} else

				if(Objects.equals("x12345", name)){
					checkFields(join(dataFieldNames, "x12", "x123", "x1234"), fields);
				} else

				{
					fail();
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

				if(Objects.equals("first", id)){
					checkFields(dataFieldNames, fields);
				} else

				if(Objects.equals("second", id)){
					checkFields(join(dataFieldNames, "x1_squared"), fields);
				} else

				if(Objects.equals("third", id)){
					checkFields(dataFieldNames, fields);
				} else

				{
					fail();
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

				Model model = segment.requireModel();

				Output output = model.getOutput();
				if(output != null && output.hasOutputFields()){
					fields = getFields(output);
				} else

				{
					fields = getFields();
				}

				String id = segment.getId();

				if(Objects.equals("first", id)){
					checkFields(dataFieldNames, fields);
				} else

				if(Objects.equals("second", id)){
					checkFields(join(dataFieldNames, "second_output"), fields);
				} else

				if(Objects.equals("third", id)){
					checkFields(join(dataFieldNames, "third_output"), fields);
				} else

				{
					fail();
				}

				return super.visit(variableWeight);
			}
		};

		variableWeightResolver.applyTo(pmml);
	}

	static
	private void checkFields(Collection<String> names, Collection<Field<?>> fields){
		Set<String> fieldNames = fields.stream()
			.map(field -> field.requireName())
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