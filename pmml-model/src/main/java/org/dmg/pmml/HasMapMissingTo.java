/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML expression elements that specify the <code>mapMissingTo</code> attribute.
 * </p>
 */
public interface HasMapMissingTo<E extends Expression & HasMapMissingTo<E, V>, V> {

	V getMapMissingTo();

	E setMapMissingTo(V mapMissingTo);
}