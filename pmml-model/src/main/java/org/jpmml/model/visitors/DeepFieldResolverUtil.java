/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.Field;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.MiningModel;
import org.dmg.pmml.Model;
import org.dmg.pmml.MultipleModelMethodType;
import org.dmg.pmml.Output;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.Segment;
import org.dmg.pmml.Segmentation;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.FieldUtil;

public class DeepFieldResolverUtil {

	private DeepFieldResolverUtil(){
	}

	static
	public Set<Field> getActiveFields(DeepFieldResolver resolver, MiningModel miningModel){
		Set<Field> modelFields = getModelFields(resolver, miningModel);

		Set<Field> activeFields = new HashSet<>();

		Segmentation segmentation = miningModel.getSegmentation();

		List<Segment> segments = segmentation.getSegments();
		for(Segment segment : segments){
			Predicate predicate = segment.getPredicate();

			if(predicate != null){
				Set<FieldName> names = getFieldNames(predicate);

				if(names.size() > 0){
					Set<Field> segmentFields = resolver.getFields(miningModel, segmentation, segment);

					activeFields.addAll(FieldUtil.selectAll(segmentFields, names));
				}
			}
		}

		Output output = miningModel.getOutput();
		if(output != null){
			Set<FieldName> names = getFieldNames(output);

			if(names.size() > 0){
				activeFields.addAll(FieldUtil.selectAll(modelFields, names));
			}

			activeFields.removeAll(output.getOutputFields());
		}

		MultipleModelMethodType multipleModelMethod = segmentation.getMultipleModelMethod();
		switch(multipleModelMethod){
			case MODEL_CHAIN:
				Set<Field> segmentationFields = resolver.getFields(miningModel, segmentation);
				segmentationFields.removeAll(modelFields);

				activeFields.removeAll(segmentationFields);
				break;
			default:
				break;
		}

		return activeFields;
	}

	static
	public Set<Field> getActiveFields(DeepFieldResolver resolver, Model model){
		Set<Field> modelFields = getModelFields(resolver, model);

		FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder(){

			@Override
			public VisitorAction visit(LocalTransformations localTransformations){
				return VisitorAction.SKIP;
			}
		};
		fieldReferenceFinder.applyTo(model);

		Set<Field> activeFields = FieldUtil.selectAll(modelFields, fieldReferenceFinder.getFieldNames());

		Output output = model.getOutput();
		if(output != null){
			activeFields.removeAll(output.getOutputFields());
		}

		return activeFields;
	}

	static
	private Set<Field> getModelFields(DeepFieldResolver resolver, Model model){
		Output output = model.getOutput();

		if(output != null && output.hasOutputFields()){
			return resolver.getFields(model, output);
		} else

		{
			return resolver.getFields(model);
		}
	}

	static
	private Set<FieldName> getFieldNames(PMMLObject object){
		FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder();
		fieldReferenceFinder.applyTo(object);

		return fieldReferenceFinder.getFieldNames();
	}
}