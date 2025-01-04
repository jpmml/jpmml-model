/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml.time_series;

import org.dmg.pmml.PMMLObject;

public interface HasRequiredMA<E extends PMMLObject & HasRequiredMA<E>> extends HasMA<E> {

	Integer requireQ();

	MA requireMA();
}