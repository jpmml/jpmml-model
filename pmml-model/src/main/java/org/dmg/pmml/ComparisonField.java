/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class ComparisonField<E extends ComparisonField<E>> extends PMMLObject implements HasFieldReference<E> {

	abstract
	public Double getFieldWeight();

	abstract
	public E setFieldWeight(Double fieldWeight);

	abstract
	public CompareFunction getCompareFunction();

	abstract
	public E setCompareFunction(CompareFunction compareFunction);

	public Double getSimilarityScale(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the <code>similarityScale</code> attribute is not supported.
	 */
	public E setSimilarityScale(Double similarityScale){
		throw new UnsupportedOperationException();
	}
}