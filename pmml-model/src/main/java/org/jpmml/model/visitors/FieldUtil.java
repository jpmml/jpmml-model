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
	public Set<FieldName> nameSet(Collection<? extends Field> fields){
		Map<FieldName, ? extends Field> result = nameMap(fields);

		return result.keySet();
	}

	static
	public <F extends Field> Map<FieldName, F> nameMap(Collection<? extends F> fields){
		Map<FieldName, F> result = new LinkedHashMap<>();

		for(F field : fields){
			FieldName name = field.getName();

			F previousField = result.put(name, field);
			if(previousField != null){
				throw new IllegalArgumentException("Fields " + format(field) + " and " + format(previousField) + " have the same name " + name);
			}
		}

		return result;
	}

	static
	public <F extends Field> Set<F> selectAll(Collection<? extends F> fields, Set<FieldName> names){
		return selectAll(fields, names, false);
	}

	static
	public <F extends Field> Set<F> selectAll(Collection<? extends F> fields, Set<FieldName> names, boolean allowPartialSelection){
		Map<FieldName, F> result = nameMap(fields);

		if(!allowPartialSelection && !(result.keySet()).containsAll(names)){
			Set<FieldName> unmatchedNames = new LinkedHashSet<>(names);
			unmatchedNames.removeAll(result.keySet());

			throw new IllegalArgumentException("Empty selection for names " + unmatchedNames);
		}

		(result.keySet()).retainAll(names);

		return new LinkedHashSet<>(result.values());
	}

	static
	private String format(Field field){
		return String.valueOf(field);
	}
}