/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasRequiredOpType<E extends PMMLObject & HasRequiredOpType<E>> extends HasOpType<E> {

	OpType requireOpType();
}