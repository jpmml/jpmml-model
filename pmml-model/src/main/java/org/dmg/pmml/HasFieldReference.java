/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML expression elements that specify the <code>field</code> attribute.
 * </p>
 */
public interface HasFieldReference<E extends Expression & HasFieldReference<E>> {

	FieldName getField();

	E setField(FieldName field);
}