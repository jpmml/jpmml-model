/*
 * Copyright (c) 2026 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasTargetCategory<E extends PMMLObject & HasTargetCategory<E>> {

	Object getTargetCategory();

	E setTargetCategory(Object targetCategory);
}