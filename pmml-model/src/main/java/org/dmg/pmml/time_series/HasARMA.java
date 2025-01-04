/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml.time_series;

import org.dmg.pmml.PMMLObject;

public interface HasARMA<E extends PMMLObject & HasARMA<E>> extends HasAR<E>, HasMA<E> {
}