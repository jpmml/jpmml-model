/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasRecordCount<E extends PMMLObject & HasRecordCount<E>> {

	Number getRecordCount();

	E setRecordCount(Number recordCount);
}