/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasDataType<E extends PMMLObject & HasDataType<E>> {

	DataType getDataType();

	E setDataType(DataType dataType);
}