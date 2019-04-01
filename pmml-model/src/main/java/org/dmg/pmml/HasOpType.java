/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasOpType<E extends PMMLObject & HasOpType<E>> {

	default
	OpType getOpType(OpType defaultOpType){
		OpType opType = getOpType();

		if(opType == null){
			return defaultOpType;
		}

		return opType;
	}

	OpType getOpType();

	E setOpType(OpType opType);
}