/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collections;
import java.util.Map;

import org.dmg.pmml.Field;
import org.dmg.pmml.FieldName;

/**
 * <p>
 * A Visitor that renames a {@link Field} and all references to it.
 * </p>
 */
public class FieldRenamer extends FieldNameFilterer {

	private Map<FieldName, FieldName> mappings = null;


	public FieldRenamer(FieldName from, FieldName to){
		this(Collections.singletonMap(from, to));
	}

	public FieldRenamer(Map<FieldName, FieldName> mappings){
		setMappings(mappings);
	}

	@Override
	public FieldName filter(FieldName name){
		Map<FieldName, FieldName> mappings = getMappings();

		if(name != null){
			FieldName updatedName = mappings.get(name);

			if(updatedName != null){
				return updatedName;
			}
		}

		return name;
	}

	public Map<FieldName, FieldName> getMappings(){
		return this.mappings;
	}

	private void setMappings(Map<FieldName, FieldName> mappings){
		this.mappings = mappings;
	}
}