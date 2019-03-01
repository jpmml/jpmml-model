/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.DerivedField;
import org.dmg.pmml.Field;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.TransformationDictionary;

/**
 * <p>
 * A Visitor that removes redundant {@link DerivedField derived fields} from global and local transformation dictionaries.
 * </p>
 */
public class TransformationDictionaryCleaner extends ModelCleaner {

	@Override
	public PMMLObject popParent(){
		PMMLObject parent = super.popParent();

		if(parent instanceof Model){
			Model model = (Model)parent;

			LocalTransformations localTransformations = model.getLocalTransformations();
			if(localTransformations != null){
				processLocalTransformations(localTransformations);

				if(!localTransformations.hasDerivedFields()){
					model.setLocalTransformations(null);
				}
			}
		} else

		if(parent instanceof PMML){
			PMML pmml = (PMML)parent;

			TransformationDictionary transformationDictionary = pmml.getTransformationDictionary();
			if(transformationDictionary != null){
				processTransformationDictionary(transformationDictionary);

				if(!transformationDictionary.hasDefineFunctions() && !transformationDictionary.hasDerivedFields()){
					pmml.setTransformationDictionary(null);
				}
			}
		}

		return parent;
	}

	private void processLocalTransformations(LocalTransformations localTransformations){

		if(localTransformations.hasDerivedFields()){
			List<DerivedField> derivedFields = localTransformations.getDerivedFields();

			Set<DerivedField> activeDerivedFields = getActiveDerivedFields(derivedFields);

			derivedFields.retainAll(activeDerivedFields);
		}
	}

	private void processTransformationDictionary(TransformationDictionary transformationDictionary){

		if(transformationDictionary.hasDerivedFields()){
			List<DerivedField> derivedFields = transformationDictionary.getDerivedFields();

			Set<DerivedField> activeDerivedFields = getActiveDerivedFields(derivedFields);

			derivedFields.retainAll(activeDerivedFields);
		}
	}

	private Set<DerivedField> getActiveDerivedFields(Collection<DerivedField> derivedFields){
		FieldDependencyResolver fieldDependencyResolver = getFieldDependencyResolver();

		Set<Field<?>> activeFields = getActiveFields();

		Set<DerivedField> activeDerivedFields = new HashSet<>(derivedFields);
		activeDerivedFields.retainAll(activeFields);

		while(true){
			Set<Field<?>> fields = new HashSet<>(activeDerivedFields);

			fieldDependencyResolver.expand(fields, activeDerivedFields);

			activeFields.addAll(fields);

			// Removes all fields that are not derived fields
			fields.retainAll(derivedFields);

			if(fields.isEmpty()){
				break;
			}

			activeDerivedFields.addAll((Set)fields);
		}

		return activeDerivedFields;
	}
}