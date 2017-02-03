/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class TypeDefinitionField extends Field {

	abstract
	public boolean hasValues();

	abstract
	public List<Value> getValues();

	abstract
	public TypeDefinitionField addValues(Value... values);

	abstract
	public boolean hasIntervals();

	abstract
	public List<Interval> getIntervals();

	abstract
	public TypeDefinitionField addIntervals(Interval... intervals);
}