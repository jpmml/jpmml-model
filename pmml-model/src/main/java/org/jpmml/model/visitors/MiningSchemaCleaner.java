/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.Field;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.FieldUsageType;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.MiningModel;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Model;
import org.dmg.pmml.MultipleModelMethodType;
import org.dmg.pmml.Output;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.Segment;
import org.dmg.pmml.Segmentation;
import org.jpmml.model.FieldUtil;

/**
 * <p>
 * A Visitor that removes redundant {@link MiningField mining fields} from the {@link MiningSchema mining schema}.
 * </p>
 */
public class MiningSchemaCleaner extends DeepFieldResolver {

	@Override
	public PMMLObject popParent(){
		PMMLObject parent = super.popParent();

		if(parent instanceof MiningModel){
			MiningModel miningModel = (MiningModel)parent;

			Set<FieldName> activeFieldNames = processMiningModel(miningModel);

			clean(miningModel, activeFieldNames);
		} else

		if(parent instanceof Model){
			Model model = (Model)parent;

			Set<FieldName> activeFieldNames = processModel(model);

			clean(model, activeFieldNames);
		}

		return parent;
	}

	private Set<FieldName> processMiningModel(MiningModel miningModel){
		Set<FieldName> activeFieldNames = new LinkedHashSet<>();

		Segmentation segmentation = miningModel.getSegmentation();

		List<Segment> segments = segmentation.getSegments();
		for(Segment segment : segments){
			Predicate predicate = segment.getPredicate();
			if(predicate != null){
				activeFieldNames.addAll(getFieldNames(predicate));
			}

			Model model = segment.getModel();
			if(model != null){
				MiningSchema miningSchema = model.getMiningSchema();

				List<MiningField> miningFields = miningSchema.getMiningFields();
				for(MiningField miningField : miningFields){
					FieldName name = miningField.getName();

					FieldUsageType fieldUsage = miningField.getUsageType();
					switch(fieldUsage){
						case ACTIVE:
							activeFieldNames.add(name);
							break;
						default:
							break;
					}
				}
			}
		}

		Output output = miningModel.getOutput();
		if(output != null){
			activeFieldNames.addAll(getFieldNames(output));
		}

		Set<Field> modelFields = getModelFields(miningModel);

		MultipleModelMethodType multipleModelMethod = segmentation.getMultipleModelMethod();
		switch(multipleModelMethod){
			case MODEL_CHAIN:
				Set<Field> segmentationFields = getFields(miningModel, segmentation);
				segmentationFields.removeAll(modelFields);

				if(segmentationFields.size() > 0){
					activeFieldNames.removeAll(FieldUtil.nameSet(segmentationFields));
				}
				break;
			default:
				break;
		}

		Set<Field> activeFields = FieldUtil.selectAll(modelFields, activeFieldNames);

		return processModel(miningModel, activeFields);
	}

	private Set<FieldName> processModel(Model model){
		FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder();
		fieldReferenceFinder.applyTo(model);

		Set<Field> modelFields = getModelFields(model);

		Set<Field> activeFields = FieldUtil.selectAll(modelFields, fieldReferenceFinder.getFieldNames());

		return processModel(model, activeFields);
	}

	private Set<Field> getModelFields(Model model){
		Output output = model.getOutput();

		if(output != null){
			return getFields(model, output);
		}

		return getFields(model);
	}

	private Set<FieldName> processModel(Model model, Set<Field> activeFields){
		FieldDependencyResolver fieldDependencyResolver = getFieldDependencyResolver();
		fieldDependencyResolver.expand(activeFields, fieldDependencyResolver.getGlobalDerivedFields());

		LocalTransformations localTransformations = model.getLocalTransformations();
		if(localTransformations != null && localTransformations.hasDerivedFields()){
			fieldDependencyResolver.expand(activeFields, new HashSet<>(localTransformations.getDerivedFields()));

			activeFields.removeAll(localTransformations.getDerivedFields());
		}

		Output output = model.getOutput();
		if(output != null && output.hasOutputFields()){
			activeFields.removeAll(output.getOutputFields());
		}

		return FieldUtil.nameSet(activeFields);
	}

	private void clean(Model model, Set<FieldName> activeFieldNames){
		MiningSchema miningSchema = model.getMiningSchema();

		activeFieldNames = new LinkedHashSet<>(activeFieldNames);

		List<MiningField> miningFields = miningSchema.getMiningFields();

		for(Iterator<MiningField> it = miningFields.iterator(); it.hasNext(); ){
			MiningField miningField = it.next();

			FieldName name = miningField.getName();

			FieldUsageType fieldUsage = miningField.getUsageType();
			switch(fieldUsage){
				case ACTIVE:
					if(!(activeFieldNames).contains(name)){
						it.remove();
					}
					break;
				default:
					break;
			}

			activeFieldNames.remove(name);
		}

		for(FieldName activeFieldName : activeFieldNames){
			MiningField miningField = new MiningField(activeFieldName);

			miningSchema.addMiningFields(miningField);
		}
	}

	static
	private Set<FieldName> getFieldNames(PMMLObject object){
		FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder();
		fieldReferenceFinder.applyTo(object);

		return fieldReferenceFinder.getFieldNames();
	}
}