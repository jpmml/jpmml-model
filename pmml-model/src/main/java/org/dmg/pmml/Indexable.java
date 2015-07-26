/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML elements that specify a primary key-like attribute.
 * </p>
 */
public interface Indexable<K> {

	/**
	 * @return The value of the primary key-like attribute. Never <code>null</code>.
	 */
	K getKey();
}