/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class ModelQuality extends PMMLObject {

	abstract
	public String getDataName();

	abstract
	public ModelQuality setDataName(String dataName);
}