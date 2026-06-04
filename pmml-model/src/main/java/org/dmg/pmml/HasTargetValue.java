/*
 * Copyright (c) 2026 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * @see HasTargetFieldReference
 */
public interface HasTargetValue<E extends PMMLObject & HasTargetValue<E>> extends HasValue<E> {
}