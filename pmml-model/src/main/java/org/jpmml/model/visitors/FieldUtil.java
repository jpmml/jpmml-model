/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

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
	public <F extends Field<?>> Set<FieldName> nameSet(Collection<? extends F> fields){
		Map<FieldName, F> fieldMap = new LinkedHashMap<>(2 * fields.size());

		for(F field : fields){
			FieldName name = field.getName();

			F previousField = fieldMap.put(name, field);
			if(previousField != null){
				throw new IllegalArgumentException("Fields " + format(field) + " and " + format(previousField) + " have the same name " + name);
			}
		}

		return fieldMap.keySet();
	}

	static
	public <F extends Field<?>> Collection<F> selectAll(Collection<? extends F> fields, Set<FieldName> names){
		return selectAll(fields, names, false);
	}

	static
	public <F extends Field<?>> Collection<F> selectAll(Collection<? extends F> fields, Set<FieldName> names, boolean allowPartialSelection){
		Map<FieldName, F> fieldMap = new LinkedHashMap<>(2 * names.size());

		for(F field : fields){
			FieldName name = field.getName();

			if(!names.contains(name)){
				continue;
			}

			F previousField = fieldMap.put(name, field);
			if(previousField != null){
				throw new IllegalArgumentException("Fields " + format(field) + " and " + format(previousField) + " have the same name " + name);
			}
		}

		if(!(allowPartialSelection) && (fieldMap.size() < names.size())){
			Set<FieldName> unmatchedNames = new LinkedHashSet<>(names);
			unmatchedNames.removeAll(fieldMap.keySet());

			throw new IllegalArgumentException("Name(s) " + unmatchedNames + " do not match any fields");
		}

		return fieldMap.values();
	}

	static
	private String format(Field<?> field){
		return String.valueOf(field);
	}
}