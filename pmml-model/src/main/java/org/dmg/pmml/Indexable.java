/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * A marker interface for PMML elements that specify a primary key-like attribute.
 */
public interface Indexable<K> {

	/**
	 * Gets the value of the primary key-like attribute.
	 */
	K getKey();
}