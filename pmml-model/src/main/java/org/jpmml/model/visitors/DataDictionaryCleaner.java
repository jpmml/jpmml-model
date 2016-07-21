/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.FieldUtil;

/**
 * <p>
 * A Visitor that removes redundant {@link DataField data fields} from the {@link DataDictionary data dictionary}.
 * </p>
 */
public class DataDictionaryCleaner extends DeepFieldResolver {

	@Override
	public PMMLObject popParent(){
		PMMLObject parent = super.popParent();

		if(parent instanceof DataDictionary){
			DataDictionary dataDictionary = (DataDictionary)parent;

			Set<FieldName> fieldNames = processDataDictionary(dataDictionary);

			FieldUtil.retainAll(dataDictionary.getDataFields(), fieldNames);
		}

		return parent;
	}

	private Set<FieldName> processDataDictionary(DataDictionary dataDictionary){
		PMML pmml = (PMML)VisitorUtil.getParent(this);

		final
		Set<FieldName> names = new HashSet<>();

		Visitor visitor = new AbstractModelVisitor(){

			@Override
			public VisitorAction visit(Model model){
				MiningSchema miningSchema = model.getMiningSchema();

				if(miningSchema.hasMiningFields()){
					List<MiningField> miningFields = miningSchema.getMiningFields();

					for(MiningField miningField : miningFields){
						FieldName name = miningField.getName();

						names.add(name);
					}
				}

				return VisitorAction.SKIP;
			}
		};
		visitor.applyTo(pmml);

		Set<DataField> fields = FieldUtil.selectAll(dataDictionary.getDataFields(), names);

		return FieldUtil.nameSet(fields);
	}
}