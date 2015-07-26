/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML elements that specify the <code>id</code> attribute.
 * </p>
 */
public interface HasId {

	/**
	 * @return The value of the <code>id</code> attribute. Could be <code>null</code>.
	 */
	String getId();

	HasId setId(String id);
}
