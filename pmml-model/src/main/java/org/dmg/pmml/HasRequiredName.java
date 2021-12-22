/*
 * Copyright (c) Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasRequiredName<E extends PMMLObject & HasRequiredName<E>> extends HasName<E>, Indexable<String> {

	String requireName();

	@Override
	default
	public String getKey(){
		return requireName();
	}
}