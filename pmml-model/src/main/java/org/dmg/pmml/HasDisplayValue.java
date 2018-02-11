/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasDisplayValue<E extends PMMLObject & HasDisplayValue<E>> {

	String getDisplayValue();

	E setDisplayValue(String displayValue);
}