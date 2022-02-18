/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

import org.jpmml.model.UnsupportedElementException;

public interface HasModel<E extends PMMLObject & HasModel<E>> {

	/**
	 * @throws UnsupportedElementException If the {@link Model} child element is not assignment-compatible with the assumed type.
	 */
	default
	<M extends Model> M requireModel(Class<? extends M> clazz){
		Model model = requireModel();

		if(!clazz.isInstance(model)){
			throw new UnsupportedElementException(model);
		}

		return clazz.cast(model);
	}

	Model requireModel();

	Model getModel();

	E setModel(Model model);
}