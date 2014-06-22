/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class ComparisonField extends PMMLObject {

	abstract
	public FieldName getField();

	abstract
	public void setField(FieldName field);

	abstract
	public double getFieldWeight();

	abstract
	public void setFieldWeight(Double fieldWeight);

	abstract
	public CompareFunctionType getCompareFunction();

	abstract
	public void setCompareFunction(CompareFunctionType compareFunction);

	abstract
	public Double getSimilarityScale();

	abstract
	public void setSimilarityScale(Double similarityScale);
}