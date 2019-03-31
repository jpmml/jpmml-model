/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasDataType<E extends PMMLObject & HasDataType<E>> {

	default
	DataType getDataType(DataType defaultDataType){
		DataType dataType = getDataType();

		if(dataType == null){
			return defaultDataType;
		}

		return dataType;
	}

	DataType getDataType();

	E setDataType(DataType dataType);
}