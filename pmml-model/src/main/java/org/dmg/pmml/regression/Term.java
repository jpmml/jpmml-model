/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml.regression;

import javax.xml.bind.annotation.XmlTransient;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.HasExtensions;
import org.dmg.pmml.HasName;
import org.dmg.pmml.PMMLObject;

@XmlTransient
abstract
public class Term extends PMMLObject implements HasName, HasExtensions {

	@Override
	abstract
	public FieldName getName();

	@Override
	abstract
	public Term setName(FieldName name);
}