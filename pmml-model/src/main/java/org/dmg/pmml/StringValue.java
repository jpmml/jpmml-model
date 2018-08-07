/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

public interface StringValue<E extends Enum<E> & StringValue<E>> {

	String value();
}