/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml.general_regression;

import jakarta.xml.bind.annotation.XmlTransient;
import org.dmg.pmml.HasTargetCategory;
import org.dmg.pmml.PMMLObject;

@XmlTransient
abstract
public class ParameterCell extends PMMLObject implements HasTargetCategory<ParameterCell> {

	abstract
	public String requireParameterName();

	abstract
	public String getParameterName();

	abstract
	public ParameterCell setParameterName(String parameterName);
}