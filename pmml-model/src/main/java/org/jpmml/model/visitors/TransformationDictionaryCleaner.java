/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.DerivedField;
import org.dmg.pmml.Field;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.FieldUtil;

/**
 * <p>
 * A Visitor that removes redundant {@link DerivedField derived fields} from global and local transformation dictionaries.
 * </p>
 */
public class TransformationDictionaryCleaner extends DeepFieldResolver {

	@Override
	public PMMLObject popParent(){
		PMMLObject parent = super.popParent();

		if(parent instanceof LocalTransformations){
			LocalTransformations localTransformations = (LocalTransformations)parent;

			if(localTransformations.hasDerivedFields()){
				Set<FieldName> activeFieldNames = processLocalTransformations(localTransformations);

				clean(localTransformations.getDerivedFields(), activeFieldNames);
			}
		} else

		if(parent instanceof Model){
			Model model = (Model)parent;

			LocalTransformations localTransformations = model.getLocalTransformations();
			if(localTransformations != null && isEmpty(localTransformations)){
				model.setLocalTransformations(null);
			}
		} else

		if(parent instanceof PMML){
			PMML pmml = (PMML)parent;

			TransformationDictionary transformationDictionary = pmml.getTransformationDictionary();
			if(transformationDictionary != null && isEmpty(transformationDictionary)){
				pmml.setTransformationDictionary(null);
			}
		} else

		if(parent instanceof TransformationDictionary){
			TransformationDictionary transformationDictionary = (TransformationDictionary)parent;

			if(transformationDictionary.hasDerivedFields()){
				Set<FieldName> activeFieldNames = processTransformationDictionary(transformationDictionary);

				clean(transformationDictionary.getDerivedFields(), activeFieldNames);
			}
		}

		return parent;
	}

	private boolean isEmpty(LocalTransformations localTransformations){
		return !localTransformations.hasDerivedFields();
	}

	private boolean isEmpty(TransformationDictionary transformationDictionary){
		return !transformationDictionary.hasDefineFunctions() && !transformationDictionary.hasDerivedFields();
	}

	private Set<FieldName> processLocalTransformations(final LocalTransformations localTransformations){
		Model model = (Model)VisitorUtil.getParent(this);

		FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder(){

			private Set<LocalTransformations> ignoredLocalTransformations = Collections.singleton(localTransformations);


			@Override
			public VisitorAction visit(LocalTransformations localTransformations){

				if(this.ignoredLocalTransformations.contains(localTransformations)){
					return VisitorAction.SKIP;
				}

				return super.visit(localTransformations);
			}
		};
		fieldReferenceFinder.applyTo(model);

		return processDerivedFields(new LinkedHashSet<>(localTransformations.getDerivedFields()), fieldReferenceFinder.getFieldNames());
	}

	private Set<FieldName> processTransformationDictionary(TransformationDictionary transformationDictionary){
		PMML pmml = (PMML)VisitorUtil.getParent(this);

		FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder(){

			@Override
			public VisitorAction visit(TransformationDictionary transformationDictionary){
				return VisitorAction.SKIP;
			}
		};
		fieldReferenceFinder.applyTo(pmml);

		return processDerivedFields(new LinkedHashSet<>(transformationDictionary.getDerivedFields()), fieldReferenceFinder.getFieldNames());
	}

	private Set<FieldName> processDerivedFields(Set<DerivedField> derivedFields, Set<FieldName> activeFieldNames){
		FieldDependencyResolver fieldDependencyResolver = getFieldDependencyResolver();

		Set<DerivedField> activeDerivedFields = FieldUtil.selectAll(derivedFields, activeFieldNames, true);

		while(true){
			Set<Field> fields = new LinkedHashSet<Field>(activeDerivedFields);

			fieldDependencyResolver.expand(fields, activeDerivedFields);

			// Removes all fields that are not derived fields
			fields.retainAll(derivedFields);

			if(fields.isEmpty()){
				break;
			}

			activeDerivedFields.addAll((Set)fields);
		}

		return FieldUtil.nameSet(activeDerivedFields);
	}

	private void clean(List<DerivedField> derivedFields, Set<FieldName> activeFieldNames){

		for(Iterator<DerivedField> it = derivedFields.iterator(); it.hasNext(); ){
			DerivedField derivedField = it.next();

			FieldName name = derivedField.getName();

			if(!(activeFieldNames.contains(name))){
				it.remove();
			}
		}
	}
}