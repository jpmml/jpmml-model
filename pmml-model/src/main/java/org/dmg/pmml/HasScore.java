/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasScore<E extends PMMLObject & HasScore<E>> {

	Object getScore();

	E setScore(Object score);
}