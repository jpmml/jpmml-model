/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasModel<E extends PMMLObject & HasModel<E>> {

	Model requireModel();

	Model getModel();

	E setModel(Model model);
}