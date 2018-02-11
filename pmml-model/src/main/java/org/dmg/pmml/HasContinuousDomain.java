/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

public interface HasContinuousDomain<E extends Field<E> & HasContinuousDomain<E>> {

	List<Interval> getIntervals();

	boolean hasIntervals();

	E addIntervals(Interval... intervals);
}