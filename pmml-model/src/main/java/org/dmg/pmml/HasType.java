/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasType<E extends PMMLObject & HasType<E>> extends HasDataType<E>, HasOpType<E> {
}