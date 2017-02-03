/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML elements that represent a field value.
 * </p>
 */
public interface HasValue<E extends PMMLObject & HasValue<E>> {

	/**
	 * @return The name of the field.
	 *
	 * @see DataField
	 * @see DerivedField
	 * @see OutputField
	 */
	FieldName getField();

	String getValue();

	E setValue(String value);
}