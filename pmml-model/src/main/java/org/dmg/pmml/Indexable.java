/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML elements that specify a primary key-like attribute.
 * </p>
 *
 * <p>
 * In a collection of {@link Indexable} elements, all key values, including the <code>null</code> key value, must be unique.
 * </p>
 *
 * @see HasId
 */
public interface Indexable<K> {

	/**
	 * @return The value of the primary key-like attribute. Could be <code>null</code>.
	 */
	K getKey();
}