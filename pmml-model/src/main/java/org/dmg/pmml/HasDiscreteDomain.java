/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

public interface HasDiscreteDomain<E extends Field<E> & HasDiscreteDomain<E>> {

	boolean hasValues();

	List<Value> getValues();

	@SuppressWarnings("unchecked")
	default
	E addValues(Value.Property property, Object... values){
		List<Value> pmmlValues = getValues();

		for(Object value : values){
			Value pmmlValue = new Value(value)
				.setProperty(property);

			pmmlValues.add(pmmlValue);
		}

		return (E)this;
	}

	E addValues(Value... values);
}