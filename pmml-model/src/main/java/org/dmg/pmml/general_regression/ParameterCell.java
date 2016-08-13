/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml.general_regression;

import javax.xml.bind.annotation.XmlTransient;

import org.dmg.pmml.HasExtensions;
import org.dmg.pmml.PMMLObject;

@XmlTransient
abstract
public class ParameterCell extends PMMLObject implements HasExtensions {

	abstract
	public String getParameterName();

	abstract
	public ParameterCell setParameterName(String parameterName);

	abstract
	public String getTargetCategory();

	abstract
	public ParameterCell setTargetCategory(String targetCategory);
}