/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasRequiredDataType<E extends PMMLObject & HasRequiredDataType<E>> extends HasDataType<E> {

	DataType requireDataType();
}