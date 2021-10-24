/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class ComparisonField<E extends ComparisonField<E>> extends PMMLObject implements HasFieldReference<E> {

	abstract
	public Number getFieldWeight();

	abstract
	public E setFieldWeight(Number fieldWeight);

	abstract
	public CompareFunction getCompareFunction();

	abstract
	public E setCompareFunction(CompareFunction compareFunction);

	public Number getSimilarityScale(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the <code>similarityScale</code> attribute is not supported.
	 */
	public E setSimilarityScale(Number similarityScale){
		throw new UnsupportedOperationException();
	}
}