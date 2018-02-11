/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

public interface HasDiscreteDomain<E extends Field<E> & HasDiscreteDomain<E>> {

	List<Value> getValues();

	boolean hasValues();

	E addValues(Value... values);
}