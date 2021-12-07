/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasTargetFieldReference<E extends PMMLObject & HasTargetFieldReference<E>> {

	String getTargetField();

	E setTargetField(String targetField);
}