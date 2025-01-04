/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasRequiredMatrix<E extends PMMLObject & HasRequiredMatrix<E>> extends HasMatrix<E> {

	Matrix requireMatrix();
}