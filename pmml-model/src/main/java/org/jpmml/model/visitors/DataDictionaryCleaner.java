/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.Field;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.FieldUsageType;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Visitable;
import org.jpmml.model.FieldUtil;

/**
 * <p>
 * A Visitor that removes redundant {@link DataField data fields} from the {@link DataDictionary data dictionary}.
 * </p>
 */
public class DataDictionaryCleaner extends ModelCleaner {

	private Set<Field> targetFields = new HashSet<>();


	@Override
	public void applyTo(Visitable visitable){
		this.targetFields.clear();

		super.applyTo(visitable);
	}

	@Override
	public PMMLObject popParent(){
		PMMLObject parent = super.popParent();

		if(parent instanceof Model){
			Model model = (Model)parent;

			processModel(model);
		} else

		if(parent instanceof PMML){
			PMML pmml = (PMML)parent;

			DataDictionary dataDictionary = pmml.getDataDictionary();
			if(dataDictionary != null){
				processDataDictionary(dataDictionary);
			}
		}

		return parent;
	}

	private void processModel(Model model){
		Set<Field> targetFields = getTargetFields();

		MiningSchema miningSchema = model.getMiningSchema();
		if(miningSchema != null && miningSchema.hasMiningFields()){
			Set<FieldName> targetFieldNames = new LinkedHashSet<>();

			List<MiningField> miningFields = miningSchema.getMiningFields();
			for(MiningField miningField : miningFields){
				FieldName name = miningField.getName();

				FieldUsageType fieldUsage = miningField.getUsageType();
				switch(fieldUsage){
					case TARGET:
					case PREDICTED:
						targetFieldNames.add(name);
						break;
					default:
						break;
				}
			}

			if(targetFieldNames.size() > 0){
				Set<Field> modelFields = getFields(model);

				targetFields.addAll(FieldUtil.selectAll(modelFields, targetFieldNames));
			}
		}
	}

	private void processDataDictionary(DataDictionary dataDictionary){

		if(dataDictionary.hasDataFields()){
			List<DataField> dataFields = dataDictionary.getDataFields();

			Set<DataField> usedDataFields = getUsedDataFields();

			dataFields.retainAll(usedDataFields);
		}
	}

	private Set<DataField> getUsedDataFields(){
		FieldDependencyResolver fieldDependencyResolver = getFieldDependencyResolver();

		Set<Field> usedFields = new HashSet<>(getActiveFields());
		usedFields.addAll(getTargetFields());

		fieldDependencyResolver.expand(usedFields, fieldDependencyResolver.getLocalDerivedFields());
		fieldDependencyResolver.expand(usedFields, fieldDependencyResolver.getGlobalDerivedFields());

		return (Set)usedFields;
	}

	public Set<Field> getTargetFields(){
		return this.targetFields;
	}
}