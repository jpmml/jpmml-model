/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasTargets<E extends PMMLObject & HasTargets<E>> {

	Targets getTargets();

	E setTargets(Targets targets);
}