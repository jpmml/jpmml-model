/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Field<E extends Field<E>> extends PMMLObject implements HasName<E>, HasType<E>, Indexable<FieldName> {

	public String getDisplayName(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the <code>displayName</code> attribute is not supported.
	 */
	public E setDisplayName(String displayName){
		throw new UnsupportedOperationException();
	}

	@Override
	public FieldName getKey(){
		return getName();
	}
}