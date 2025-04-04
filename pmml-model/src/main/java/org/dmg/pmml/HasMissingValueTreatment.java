/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasMissingValueTreatment<E extends PMMLObject & HasMissingValueTreatment<E>> {

	MissingValueTreatmentMethod getMissingValueTreatment();

	E setMissingValueTreatment(MissingValueTreatmentMethod missingValueTreatment);
}