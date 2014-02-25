/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.*;

@XmlTransient
abstract
public class Term extends PMMLObject implements HasName {

	@Override
	abstract
	public FieldName getName();

	@Override
	abstract
	public void setName(FieldName name);
}