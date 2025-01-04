/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasRequiredArray<E extends PMMLObject & HasRequiredArray<E>> extends HasArray<E> {

	Array requireArray();
}