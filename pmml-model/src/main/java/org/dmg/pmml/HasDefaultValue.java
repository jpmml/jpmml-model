/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML expression elements that specify the <code>defaultValue</code> attribute.
 * </p>
 */
public interface HasDefaultValue<E extends Expression & HasDefaultValue<E, V>, V> {

	V getDefaultValue();

	E setDefaultValue(V defaultValue);
}