/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasArray<E extends PMMLObject & HasArray<E>> {

	Array getArray();

	E setArray(Array array);
}