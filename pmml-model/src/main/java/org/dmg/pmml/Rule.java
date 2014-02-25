/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.*;

@XmlTransient
abstract
public class Rule extends Entity {

	abstract
	public Predicate getPredicate();

	abstract
	public void setPredicate(Predicate predicate);
}