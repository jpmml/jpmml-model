/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML elements that represent a set of field values.
 * </p>
 */
public interface HasValueSet<E extends PMMLObject & HasValueSet<E>> {

	/**
	 * @return The name of the field.
	 *
	 * @see DataField
	 * @see DerivedField
	 * @see OutputField
	 */
	FieldName getField();

	Array getArray();

	E setArray(Array array);
}