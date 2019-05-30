/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.dmg.pmml.DerivedField;
import org.dmg.pmml.Field;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.Visitable;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.MiningModel;

/**
 * <p>
 * A Visitor that relocates {@link DerivedField derived fields} to the &quot;nearest&quot; local transformation dictionary.
 * </p>
 *
 * @see TransformationDictionaryCleaner
 */
public class DerivedFieldRelocator extends DeepFieldResolver {

	private Map<DerivedField, Set<Model>> derivedFieldModels = new IdentityHashMap<>();


	@Override
	public void applyTo(Visitable visitable){
		this.derivedFieldModels.clear();

		super.applyTo(visitable);
	}

	@Override
	public PMMLObject popParent(){
		PMMLObject parent = super.popParent();

		if(parent instanceof Model){
			processModel((Model)parent);
		} else

		if(parent instanceof PMML){
			processPMML((PMML)parent);
		}

		return parent;
	}

	private void processModel(Model model){
		Set<Model> parentModels = new LinkedHashSet<>();
		parentModels.add(model);

		Deque<PMMLObject> parents = getParents();
		for(PMMLObject parent : parents){

			if(parent instanceof Model){
				Model parentModel = (Model)parent;

				parentModels.add(parentModel);
			}
		}

		Set<DerivedField> activeDerivedFields = getActiveDerivedFields(model);
		for(DerivedField activeDerivedField : activeDerivedFields){
			Set<Model> models = this.derivedFieldModels.get(activeDerivedField);

			if(models == null){
				models = new LinkedHashSet<>(parentModels);

				this.derivedFieldModels.put(activeDerivedField, models);
			} else

			{
				models.retainAll(parentModels);
			}
		}
	}

	private void processPMML(PMML pmml){
		Map<DerivedField, Model> derivedFieldScopes = new IdentityHashMap<>();

		Collection<? extends Map.Entry<DerivedField, Set<Model>>> entries = this.derivedFieldModels.entrySet();
		for(Map.Entry<DerivedField, Set<Model>> entry : entries){
			DerivedField derivedField = entry.getKey();
			Set<Model> models = entry.getValue();

			if(models.size() > 0){
				Model model = (models.iterator()).next();

				derivedFieldScopes.put(derivedField, model);
			}
		}

		Map<DerivedField, Integer> orderMap = new IdentityHashMap<>();

		Visitor indexer = new AbstractVisitor(){

			@Override
			public VisitorAction visit(DerivedField derivedField){
				Integer index = orderMap.size();

				orderMap.put(derivedField, index);

				return super.visit(derivedField);
			}

			@Override
			public VisitorAction visit(LocalTransformations localTransformations){
				Model model = (Model)getParent();

				if(localTransformations.hasDerivedFields()){
					List<DerivedField> derivedFields = localTransformations.getDerivedFields();

					for(DerivedField derivedField : derivedFields){
						Model scope = derivedFieldScopes.get(derivedField);

						if(scope != null && Objects.equals(scope, model)){
							derivedFieldScopes.remove(derivedField);
						}
					}
				}

				return super.visit(localTransformations);
			}
		};

		Visitor relocator = new AbstractVisitor(){

			@Override
			public VisitorAction visit(LocalTransformations localTransformations){
				Model model = (Model)getParent();

				if(localTransformations.hasDerivedFields()){
					List<DerivedField> derivedFields = localTransformations.getDerivedFields();

					for(Iterator<DerivedField> it = derivedFields.iterator(); it.hasNext(); ){
						DerivedField derivedField = it.next();

						Model scope = derivedFieldScopes.get(derivedField);
						if(scope != null && !Objects.equals(scope, model)){
							it.remove();
						}
					}
				}

				return super.visit(localTransformations);
			}

			@Override
			public VisitorAction visit(Model model){
				LocalTransformations localTransformations = model.getLocalTransformations();

				Collection<? extends Map.Entry<DerivedField, Model>> entries = derivedFieldScopes.entrySet();
				for(Map.Entry<DerivedField, Model> entry : entries){
					DerivedField derivedField = entry.getKey();
					Model scope = entry.getValue();

					if(!Objects.equals(scope, model)){
						continue;
					} // End if

					if(localTransformations == null){
						localTransformations = new LocalTransformations();

						model.setLocalTransformations(localTransformations);
					}

					localTransformations.addDerivedFields(derivedField);
				}

				return super.visit(model);
			}

			@Override
			public VisitorAction visit(TransformationDictionary transformationDictionary){

				if(transformationDictionary.hasDerivedFields()){
					List<DerivedField> derivedFields = transformationDictionary.getDerivedFields();

					for(Iterator<DerivedField> it = derivedFields.iterator(); it.hasNext(); ){
						DerivedField derivedField = it.next();

						Model scope = derivedFieldScopes.get(derivedField);
						if(scope != null){
							it.remove();
						}
					}
				}

				return super.visit(transformationDictionary);
			}
		};

		Visitor sorter = new AbstractVisitor(){

			@Override
			public VisitorAction visit(LocalTransformations localTransformations){

				if(localTransformations.hasDerivedFields()){
					sort(localTransformations.getDerivedFields());
				}

				return super.visit(localTransformations);
			}

			@Override
			public VisitorAction visit(TransformationDictionary transformationDictionary){

				if(transformationDictionary.hasDerivedFields()){
					sort(transformationDictionary.getDerivedFields());
				}

				return super.visit(transformationDictionary);
			}

			private void sort(List<DerivedField> derivedFields){
				Comparator<DerivedField> comparator = new Comparator<DerivedField>(){

					@Override
					public int compare(DerivedField left, DerivedField right){
						Integer leftIndex = orderMap.get(left);
						Integer rightIndex = orderMap.get(right);

						return (leftIndex).compareTo(rightIndex);
					}
				};

				derivedFields.sort(comparator);
			}
		};

		indexer.applyTo(pmml);
		relocator.applyTo(pmml);
		sorter.applyTo(pmml);
	}

	private Set<DerivedField> getActiveDerivedFields(Model model){
		FieldDependencyResolver fieldDependencyResolver = getFieldDependencyResolver();

		Set<Field<?>> activeFields;

		if(model instanceof MiningModel){
			activeFields = DeepFieldResolverUtil.getActiveFields(this, (MiningModel)model);
		} else

		{
			activeFields = DeepFieldResolverUtil.getActiveFields(this, model);
		}

		Set<Field<?>> activeDerivedFields = new HashSet<>();

		activeDerivedFields.addAll(fieldDependencyResolver.expand(activeFields, fieldDependencyResolver.getLocalDerivedFields()));
		activeDerivedFields.addAll(fieldDependencyResolver.expand(activeFields, fieldDependencyResolver.getGlobalDerivedFields()));

		return (Set)activeDerivedFields;
	}
}