/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

public interface Visitable {

	VisitorAction accept(Visitor visitor);
}