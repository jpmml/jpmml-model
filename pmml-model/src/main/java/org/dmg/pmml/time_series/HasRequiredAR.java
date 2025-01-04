/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml.time_series;

import org.dmg.pmml.PMMLObject;

public interface HasRequiredAR<E extends PMMLObject & HasRequiredAR<E>> extends HasAR<E> {

	Integer requireP();

	AR requireAR();
}