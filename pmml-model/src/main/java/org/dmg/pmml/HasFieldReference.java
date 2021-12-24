/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for PMML elements that reference a field by name.
 * </p>
 */
public interface HasFieldReference<E extends PMMLObject & HasFieldReference<E>> {

	default
	String requireField(){
		throw new UnsupportedOperationException();
	}

	String getField();

	E setField(String field);

	default
	E setField(Field<?> field){
		return setField(field != null ? field.getName() : null);
	}
}