/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasMatrix<E extends PMMLObject & HasMatrix<E>>{

	Matrix getMatrix();

	E setMatrix(Matrix matrix);
}