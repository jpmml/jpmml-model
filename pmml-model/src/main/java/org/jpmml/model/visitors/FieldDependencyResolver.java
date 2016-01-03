/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.dmg.pmml.DataField;
import org.dmg.pmml.DerivedField;
import org.dmg.pmml.Field;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.Visitable;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.FieldUtil;

/**
 * <p>
 * A Visitor that determines which fields must be visible and accessible for evaluating a field.
 * </p>
 */
public class FieldDependencyResolver extends FieldResolver {

	private Map<Field, Set<Field>> dependencies = new LinkedHashMap<>();

	private Set<DerivedField> globalDerivedFields = new HashSet<>();

	private Set<DerivedField> localDerivedFields = new HashSet<>();


	@Override
	public void applyTo(Visitable visitable){
		this.dependencies.clear();

		this.globalDerivedFields.clear();
		this.localDerivedFields.clear();

		super.applyTo(visitable);
	}

	@Override
	public VisitorAction visit(DataField dataField){
		process(dataField);

		return super.visit(dataField);
	}

	@Override
	public VisitorAction visit(DerivedField derivedField){
		PMMLObject parent = VisitorUtil.getParent(this);

		if(VisitorUtil.isDictionary(parent)){
			process(derivedField);
		}

		return super.visit(derivedField);
	}

	@Override
	public VisitorAction visit(LocalTransformations localTransformations){

		if(localTransformations.hasDerivedFields()){
			this.localDerivedFields.addAll(localTransformations.getDerivedFields());
		}

		return super.visit(localTransformations);
	}

	@Override
	public VisitorAction visit(OutputField outputField){
		process(outputField);

		return super.visit(outputField);
	}

	@Override
	public VisitorAction visit(TransformationDictionary transformationDictionary){

		if(transformationDictionary.hasDerivedFields()){
			this.globalDerivedFields.addAll(transformationDictionary.getDerivedFields());
		}

		return super.visit(transformationDictionary);
	}

	public Set<Field> getDependencies(Field field){
		Map<Field, Set<Field>> dependencies = getDependencies();

		if(!dependencies.containsKey(field)){
			throw new IllegalArgumentException();
		}

		return dependencies.get(field);
	}

	/**
	 * @return A summary of traversed field declarations.
	 * Map keys are field elements.
	 * Map values are {@link #getFields() field resolution results} at the location where the field element is declared.
	 */
	public Map<Field, Set<Field>> getDependencies(){
		return this.dependencies;
	}

	/**
	 * <p>
	 * Expresses global DerivedField elements in terms of DataField elements.
	 * </p>
	 */
	public void expand(Set<Field> fields){
		expand(fields, this.globalDerivedFields);
	}

	public void expand(Set<Field> fields, Set<? extends Field> expandableFields){
		Set<Field> removableFields = new LinkedHashSet<>();

		for(int i = 0; true; i++){

			if(i > 1000){
				throw new IllegalStateException();
			}

			removableFields.clear();

			for(Field field : fields){

				if(expandableFields.contains(field)){
					removableFields.add(field);
				}
			}

			if(removableFields.isEmpty()){
				break;
			}

			for(Field removableField : removableFields){
				fields.addAll(getDependencies(removableField));
			}

			fields.removeAll(removableFields);
		}
	}

	private void process(Field field){
		FieldReferenceFinder fieldReferenceFinder = new FieldReferenceFinder();
		fieldReferenceFinder.applyTo(field);

		Set<Field> activeFields = FieldUtil.selectAll(getFields(), fieldReferenceFinder.getFieldNames());

		this.dependencies.put(field, activeFields);
	}
}