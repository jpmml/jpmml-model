/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

import org.jpmml.model.UnsupportedElementException;

public interface HasPredicate<E extends PMMLObject & HasPredicate<E>> {

	/**
	 * @throws UnsupportedElementException If the {@link Predicate} child element is not assignment-compatible with the assumed type.
	 */
	default
	<P extends Predicate> P requirePredicate(Class<? extends P> clazz){
		Predicate predicate = requirePredicate();

		if(!clazz.isInstance(predicate)){
			throw new UnsupportedElementException(predicate);
		}

		return clazz.cast(predicate);
	}

	Predicate requirePredicate();

	Predicate getPredicate();

	E setPredicate(Predicate predicate);
}