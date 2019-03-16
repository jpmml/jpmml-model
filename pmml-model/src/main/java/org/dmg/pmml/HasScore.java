/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasScore<E extends PMMLObject & HasScore<E>> {

	default
	boolean hasScore(){
		Object score = getScore();

		return (score != null);
	}

	Object getScore();

	E setScore(Object score);
}