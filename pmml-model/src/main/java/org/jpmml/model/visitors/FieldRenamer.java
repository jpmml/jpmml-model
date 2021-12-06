/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.dmg.pmml.Field;

/**
 * <p>
 * A Visitor that renames a {@link Field} and all references to it.
 * </p>
 */
public class FieldRenamer extends FieldNameFilterer {

	private Map<String, String> mappings = null;


	public FieldRenamer(String from, String to){
		this(Collections.singletonMap(from, to));
	}

	public FieldRenamer(Map<String, String> mappings){
		setMappings(mappings);
	}

	@Override
	public String filter(String name){
		Map<String, String> mappings = getMappings();

		if(name != null){
			String updatedName = mappings.get(name);

			if(updatedName != null){
				return updatedName;
			}
		}

		return name;
	}

	public Map<String, String> getMappings(){
		return this.mappings;
	}

	private void setMappings(Map<String, String> mappings){
		this.mappings = Objects.requireNonNull(mappings);
	}
}