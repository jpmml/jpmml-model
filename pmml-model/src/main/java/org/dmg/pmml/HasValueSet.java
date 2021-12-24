/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * @see HasFieldReference
 */
public interface HasValueSet<E extends PMMLObject & HasValueSet<E>> {

	Array requireArray();

	Array getArray();

	E setArray(Array array);
}