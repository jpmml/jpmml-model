/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasOpType<E extends PMMLObject & HasOpType<E>> {

	OpType getOpType();

	E setOpType(OpType opType);
}