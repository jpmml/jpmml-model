/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml.rule_set;

import javax.xml.bind.annotation.XmlTransient;

import org.dmg.pmml.Entity;
import org.dmg.pmml.Predicate;

@XmlTransient
abstract
public class Rule extends Entity {

	/**
	 * @throws UnsupportedOperationException If the <code>id</code> attribute is not supported.
	 */
	@Override
	public String getId(){
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException If the <code>id</code> attribute is not supported.
	 */
	@Override
	public Rule setId(String id){
		throw new UnsupportedOperationException();
	}

	abstract
	public Predicate getPredicate();

	abstract
	public Rule setPredicate(Predicate predicate);
}