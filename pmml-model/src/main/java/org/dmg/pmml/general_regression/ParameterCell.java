/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml.general_regression;

import jakarta.xml.bind.annotation.XmlTransient;
import org.dmg.pmml.PMMLObject;

@XmlTransient
abstract
public class ParameterCell extends PMMLObject {

	abstract
	public String requireParameterName();

	abstract
	public String getParameterName();

	abstract
	public ParameterCell setParameterName(String parameterName);

	abstract
	public Object getTargetCategory();

	abstract
	public ParameterCell setTargetCategory(Object targetCategory);
}