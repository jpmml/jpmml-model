/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.dmg.pmml.FieldName;

public class FieldNameUtil {

	private FieldNameUtil(){
	}

	static
	public Set<FieldName> create(String... values){
		return create(Collections.<FieldName>emptySet(), values);
	}

	static
	public Set<FieldName> create(Set<FieldName> parent, String... values){
		Set<FieldName> result = new LinkedHashSet<>(parent);

		for(String value : values){
			result.add(FieldName.create(value));
		}

		return result;
	}
}