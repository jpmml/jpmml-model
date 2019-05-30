/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

public interface HasDerivedFields<E extends PMMLObject & HasDerivedFields<E>> {

	boolean hasDerivedFields();

	List<DerivedField> getDerivedFields();

	E addDerivedFields(DerivedField... derivedFields);
}