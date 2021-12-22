/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Field<E extends Field<E>> extends PMMLObject implements HasName<E>, HasDisplayName<E>, HasType<E>, Indexable<String> {

	abstract
	public String requireName();

	@Override
	public String getKey(){
		return getName();
	}
}