/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * @see HasFieldReference
 */
public interface HasValue<E extends PMMLObject & HasValue<E>> {

	Object getValue();

	E setValue(Object value);
}