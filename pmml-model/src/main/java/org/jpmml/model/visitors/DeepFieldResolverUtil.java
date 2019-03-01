/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.Field;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.Model;
import org.dmg.pmml.Output;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.MiningModel;
import org.dmg.pmml.mining.Segment;
import org.dmg.pmml.mining.Segmentation;

public class DeepFieldResolverUtil {

	private DeepFieldResolverUtil(){
	}

	static
	public Set<Field<?>> getActiveFields(DeepFieldResolver resolver, MiningModel miningModel){
		Collection<Field<?>> modelFields = getModelFields(resolver, miningModel);

		Set<Field<?>> activeFields = new LinkedHashSet<>();

		Segmentation segmentation = miningModel.getSegmentation();

		List<Segment> segments = segmentation.getSegments();
		for(Segment segment : segments){
			Predicate predicate = segment.getPredicate();

			if(predicate != null){
				Set<FieldName> names = getFieldNames(predicate);

				if(names.size() > 0){
					Collection<Field<?>> segmentFields = resolver.getFields(miningModel, segmentation, segment);

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

		Segmentation.MultipleModelMethod multipleModelMethod = segmentation.getMultipleModelMethod();
		switch(multipleModelMethod){
			case MODEL_CHAIN:
				Collection<Field<?>> segmentationFields = resolver.getFields(miningModel, segmentation);
				segmentationFields.removeAll(modelFields);

				activeFields.removeAll(segmentationFields);
				break;
			default:
				break;
		}

		return activeFields;
	}

	static
	public Set<Field<?>> getActiveFields(DeepFieldResolver resolver, Model model){
		Collection<Field<?>> modelFields = getModelFields(resolver, model);

		Set<Field<?>> activeFields = new LinkedHashSet<>();

		FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder(){

			@Override
			public VisitorAction visit(LocalTransformations localTransformations){
				return VisitorAction.SKIP;
			}
		};
		fieldReferenceFinder.applyTo(model);

		Set<FieldName> names = fieldReferenceFinder.getFieldNames();

		activeFields.addAll(FieldUtil.selectAll(modelFields, names));

		Output output = model.getOutput();
		if(output != null){
			activeFields.removeAll(output.getOutputFields());
		}

		return activeFields;
	}

	static
	private Collection<Field<?>> getModelFields(DeepFieldResolver resolver, Model model){
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