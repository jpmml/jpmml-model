/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml.time_series;

import org.dmg.pmml.PMMLObject;

public interface HasRequiredARMA<E extends PMMLObject & HasRequiredARMA<E>> extends HasRequiredAR<E>, HasRequiredMA<E> {
}