/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Entity<V> extends PMMLObject implements HasId<Entity<V>, V> {

	public boolean hasId(){
		V id = getId();

		return (id != null);
	}
}