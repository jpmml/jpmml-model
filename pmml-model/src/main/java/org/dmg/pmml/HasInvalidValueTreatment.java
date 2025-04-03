/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasInvalidValueTreatment<E extends PMMLObject & HasInvalidValueTreatment<E>> {

	InvalidValueTreatmentMethod getInvalidValueTreatment();

	E setInvalidValueTreatment(InvalidValueTreatmentMethod invalidValueTreatment);
}