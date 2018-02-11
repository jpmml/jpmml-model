/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * @see HasFieldReference
 */
public interface HasValue<E extends PMMLObject & HasValue<E>> {

	String getValue();

	E setValue(String value);
}