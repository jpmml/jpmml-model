/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.dmg.pmml.Field;
import org.dmg.pmml.FieldName;

public class FieldUtil {

	private FieldUtil(){
	}

	static
	public Set<FieldName> nameSet(Collection<? extends Field> fields){
		Map<FieldName, Field> result = nameMap(fields);

		return result.keySet();
	}

	static
	public Map<FieldName, Field> nameMap(Collection<? extends Field> fields){
		Map<FieldName, Field> result = new LinkedHashMap<>();

		for(Field field : fields){
			FieldName name = field.getName();

			Field previousField = result.put(name, field);
			if(previousField != null){
				throw new IllegalArgumentException("Fields " + field + " and " + previousField + " have the same name " + name.getValue());
			}
		}

		return result;
	}

	static
	public Set<Field> selectAll(Collection<? extends Field> fields, Set<FieldName> names){
		Map<FieldName, Field> result = nameMap(fields);

		if(!(result.keySet()).containsAll(names)){
			throw new IllegalArgumentException();
		}

		(result.keySet()).retainAll(names);

		return new LinkedHashSet<>(result.values());
	}
}