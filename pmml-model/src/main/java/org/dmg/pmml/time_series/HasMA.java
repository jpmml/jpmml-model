/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml.time_series;

import org.dmg.pmml.PMMLObject;

interface HasMA<E extends PMMLObject & HasMA<E>> {

	Integer getQ();

	E setQ(Integer q);

	MA getMA();

	E setMA(MA ma);
}