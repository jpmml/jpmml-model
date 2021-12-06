/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasName<E extends PMMLObject & HasName<E>> {

	String getName();

	E setName(String name);
}