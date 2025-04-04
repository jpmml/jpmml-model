/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasOutlierTreatment<E extends PMMLObject & HasOutlierTreatment<E>> {

	OutlierTreatmentMethod getOutlierTreatment();

	E setOutlierTreatment(OutlierTreatmentMethod outlierTreatment);
}