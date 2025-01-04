/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml.time_series;

import org.dmg.pmml.PMMLObject;

public interface HasARIMA<E extends PMMLObject & HasARIMA<E>> extends HasARMA<E> {

	Integer getD();

	E setD(Integer d);
}