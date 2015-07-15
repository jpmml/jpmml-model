/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Rule extends Entity {

	/**
	 * @throws UnsupportedOperationException If the <code>id</code> attribute is not supported.
	 */
	@Override
	abstract
	public String getId();

	/**
	 * @throws UnsupportedOperationException If the <code>id</code> attribute is not supported.
	 */
	@Override
	abstract
	public Rule setId(String id);

	abstract
	public Predicate getPredicate();

	abstract
	public Rule setPredicate(Predicate predicate);
}