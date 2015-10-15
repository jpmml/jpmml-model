/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasPredicate {

	Predicate getPredicate();

	HasPredicate setPredicate(Predicate predicate);
}