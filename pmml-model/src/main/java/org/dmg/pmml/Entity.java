/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Entity extends PMMLObject implements HasId {

	/**
	 * @return The explicit entity identifier. Could be <code>null</code>.
	 */
	@Override
	abstract
	public String getId();

	@Override
	abstract
	public Entity setId(String id);
}