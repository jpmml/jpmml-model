/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasDisplayName<E extends PMMLObject & HasDisplayName<E>> {

	String getDisplayName();

	E setDisplayName(String displayName);
}