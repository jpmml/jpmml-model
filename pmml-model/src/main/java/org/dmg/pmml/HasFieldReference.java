/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML elements that reference a field by name.
 * </p>
 */
public interface HasFieldReference<E extends PMMLObject & HasFieldReference<E>> {

	FieldName getField();

	E setField(FieldName field);
}