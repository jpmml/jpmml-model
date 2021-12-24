/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasPredicate<E extends PMMLObject & HasPredicate<E>> {

	Predicate requirePredicate();

	Predicate getPredicate();

	E setPredicate(Predicate predicate);
}