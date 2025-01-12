/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

public interface HasArrays<E extends PMMLObject & HasArrays<E>> {

	boolean hasArrays();

	List<Array> getArrays();

	E addArrays(Array... arrays);
}