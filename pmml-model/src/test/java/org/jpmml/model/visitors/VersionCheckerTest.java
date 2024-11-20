/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.dmg.pmml.Apply;
import org.dmg.pmml.DataType;
import org.dmg.pmml.DerivedField;
import org.dmg.pmml.FieldRef;
import org.dmg.pmml.Interval;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.MathContext;
import org.dmg.pmml.MiningFunction;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.OpType;
import org.dmg.pmml.Output;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMMLFunctions;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Target;
import org.dmg.pmml.Targets;
import org.dmg.pmml.Version;
import org.dmg.pmml.mining.MiningModel;
import org.dmg.pmml.mining.Segmentation;
import org.jpmml.model.MarkupException;
import org.jpmml.model.MisplacedElementException;
import org.jpmml.model.MisplacedElementListException;
import org.jpmml.model.MissingAttributeException;
import org.jpmml.model.MissingElementException;
import org.jpmml.model.UnsupportedAttributeException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class VersionCheckerTest {

	@Test
	public void inspectApply(){
		Apply apply = new Apply()
			.setFunction(null);

		assertExceptions(apply, Version.PMML_3_1, Collections.singleton(MissingAttributeException.class));
		assertExceptions(apply, Version.PMML_3_0, Collections.emptySet());

		apply
			.setFunction(PMMLFunctions.IF);

		assertExceptions(apply, Version.PMML_4_0, Collections.emptySet());
		assertExceptions(apply, Version.PMML_3_2, Collections.singleton(UnsupportedAttributeException.class));

		apply
			.setFunction(PMMLFunctions.ATAN2);

		assertExceptions(apply, Version.XPMML, Collections.emptySet());
		assertExceptions(apply, Version.PMML_4_4, Collections.singleton(UnsupportedAttributeException.class));
	}

	@Test
	public void inspectDerivedField(){
		Interval negativeInterval = new Interval(Interval.Closure.CLOSED_OPEN)
			.setLeftMargin(-Double.MAX_VALUE)
			.setRightMargin(0d);

		Interval positiveInterval = new Interval(Interval.Closure.OPEN_CLOSED)
			.setLeftMargin(0d)
			.setRightMargin(Double.MAX_VALUE);

		DerivedField derivedField = new DerivedField("double(x)", OpType.CONTINUOUS, DataType.DOUBLE, new FieldRef("x"))
			.addIntervals(negativeInterval, positiveInterval);

		assertExceptions(derivedField, Version.PMML_4_4, Collections.singleton(MisplacedElementListException.class));
		assertExceptions(derivedField, Version.PMML_3_0, Collections.emptySet());
	}

	@Test
	public void inspectMiningModel(){
		Segmentation segmentation = new Segmentation()
			.setLocalTransformations(new LocalTransformations());

		assertExceptions(segmentation, Version.PMML_4_1, Collections.singleton(MisplacedElementException.class));
		assertExceptions(segmentation, Version.PMML_4_0, Collections.emptySet());

		segmentation
			.setMultipleModelMethod(Segmentation.MultipleModelMethod.MULTI_MODEL_CHAIN)
			.setLocalTransformations(null);

		assertExceptions(segmentation, Version.XPMML, Collections.emptySet());
		assertExceptions(segmentation, Version.PMML_4_4, Collections.singleton(UnsupportedAttributeException.class));

		segmentation
			.setMultipleModelMethod(Segmentation.MultipleModelMethod.WEIGHTED_SUM);

		assertExceptions(segmentation, Version.PMML_4_4, Collections.emptySet());
		assertExceptions(segmentation, Version.PMML_4_3, Collections.singleton(UnsupportedAttributeException.class));

		segmentation
			.setMissingPredictionTreatment(Segmentation.MissingPredictionTreatment.RETURN_MISSING);

		assertExceptions(segmentation, Version.PMML_4_4, Collections.emptySet());
		assertExceptions(segmentation, Version.PMML_4_3, Arrays.asList(UnsupportedAttributeException.class, UnsupportedAttributeException.class));

		MiningSchema miningSchema = new MiningSchema();

		assertExceptions(miningSchema, Version.XPMML, Collections.emptySet());
		assertExceptions(miningSchema, Version.PMML_4_4, Collections.singleton(MissingElementException.class));

		OutputField outputField = new OutputField()
			.setName("prediction")
			.setDataType(null);

		Output output = new Output()
			.addOutputFields(outputField);

		assertExceptions(output, Version.PMML_4_3, Collections.singleton(MissingAttributeException.class));
		assertExceptions(output, Version.PMML_4_2, Collections.emptySet());

		Target target = new Target()
			.setField(null);

		Targets targets = new Targets()
			.addTargets(target);

		assertExceptions(targets, Version.PMML_4_3, Collections.emptySet());
		assertExceptions(targets, Version.PMML_4_2, Collections.singleton(MissingAttributeException.class));

		MiningModel miningModel = new MiningModel(MiningFunction.REGRESSION, miningSchema)
			.setMathContext(MathContext.FLOAT)
			.setOutput(output)
			.setTargets(targets)
			.setSegmentation(segmentation);

		Collection<Class<? extends MarkupException>> exceptionClasses = new ArrayList<>();
		exceptionClasses.add(MissingAttributeException.class); // OutputField@dataType

		assertExceptions(miningModel, Version.XPMML, exceptionClasses);

		exceptionClasses.add(UnsupportedAttributeException.class); // MiningField@x-mathContext
		exceptionClasses.add(MissingElementException.class); // MiningSchema/MiningField

		assertExceptions(miningModel, Version.PMML_4_4, exceptionClasses);

		exceptionClasses.add(UnsupportedAttributeException.class); // Segmentation@missingPredictionTreatment
		exceptionClasses.add(UnsupportedAttributeException.class); // Segmentation@multipleModelMethod=weightedSum

		assertExceptions(miningModel, Version.PMML_4_3, exceptionClasses);

		exceptionClasses.remove(MissingAttributeException.class); // OutputField@dataType
		exceptionClasses.add(MissingAttributeException.class); // Target@field

		assertExceptions(miningModel, Version.PMML_4_2, exceptionClasses);
		assertExceptions(miningModel, Version.PMML_4_1, exceptionClasses);
		assertExceptions(miningModel, Version.PMML_4_0, exceptionClasses);
	}

	static
	private void assertExceptions(PMMLObject object, Version version, Collection<Class<? extends MarkupException>> exceptionClasses){
		VersionChecker inspector = new VersionChecker(version);
		inspector.applyTo(object);

		List<MarkupException> exceptions = inspector.getExceptions();

		assertTrue(sameElements(exceptionClasses, getClasses(exceptions)));
	}

	static
	private boolean sameElements(Collection<?> left, Collection<?> right){

		if(left.size() == right.size()){
			right = new ArrayList<>(right);

			for(Object element : left){

				if(!right.remove(element)){
					return false;
				}
			}

			return right.isEmpty();
		}

		return false;
	}

	static
	private <E> Collection<Class<?>> getClasses(Collection<E> objects){
		return objects.stream()
			.map(Object::getClass)
			.collect(Collectors.toList());
	}
}