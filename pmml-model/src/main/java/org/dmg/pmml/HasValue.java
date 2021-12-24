/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * @see HasFieldReference
 */
public interface HasValue<E extends PMMLObject & HasValue<E>> {

	default
	boolean hasValue(){
		Object value = getValue();

		return (value != null);
	}

	Object requireValue();

	Object getValue();

	E setValue(Object value);
}