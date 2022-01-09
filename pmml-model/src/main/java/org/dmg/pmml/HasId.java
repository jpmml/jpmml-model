/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML elements that specify the <code>id</code> attribute.
 * </p>
 *
 * <p>
 * In a collection of {@link HasId} elements, all non-<code>null</code> identifier values must be unique.
 * However, there may be multiple {@link HasId} elements with <code>null</code> identifier values.
 * </p>
 *
 * @see Indexable
 */
public interface HasId<E extends PMMLObject & HasId<E, V>, V> {

	/**
	 * @return The value of the <code>id</code> attribute. Could be <code>null</code>.
	 */
	V getId();

	E setId(V id);
}
