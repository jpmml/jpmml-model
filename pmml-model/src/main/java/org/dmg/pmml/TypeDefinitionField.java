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
	public List<Value> getValues();
}