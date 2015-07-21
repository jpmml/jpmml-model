/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class ComparisonField extends PMMLObject implements HasExtensions {

	abstract
	public FieldName getField();

	abstract
	public ComparisonField setField(FieldName field);

	abstract
	public Double getFieldWeight();

	abstract
	public ComparisonField setFieldWeight(Double fieldWeight);

	abstract
	public CompareFunctionType getCompareFunction();

	abstract
	public ComparisonField setCompareFunction(CompareFunctionType compareFunction);

	public Double getSimilarityScale(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the <code>similarityScale</code> attribute is not supported.
	 */
	public ComparisonField setSimilarityScale(Double similarityScale){
		throw new UnsupportedOperationException();
	}
}