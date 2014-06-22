/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class ParameterCell extends PMMLObject {

	abstract
	public String getParameterName();

	abstract
	public void setParameterName(String parameterName);

	abstract
	public String getTargetCategory();

	abstract
	public void setTargetCategory(String targetCategory);
}