/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasRequiredType<E extends PMMLObject & HasRequiredType<E>> extends HasType<E>, HasRequiredDataType<E>, HasRequiredOpType<E> {
}