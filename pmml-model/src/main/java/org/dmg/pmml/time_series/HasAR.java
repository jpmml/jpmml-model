/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml.time_series;

import org.dmg.pmml.PMMLObject;

public interface HasAR<E extends PMMLObject & HasAR<E>> {

	Integer getP();

	E setP(Integer p);

	AR getAR();

	E setAR(AR ar);
}