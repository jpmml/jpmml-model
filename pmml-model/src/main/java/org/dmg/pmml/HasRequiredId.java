/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasRequiredId<E extends PMMLObject & HasRequiredId<E>> extends HasId<E, String>, Indexable<String> {

	String requireId();

	@Override
	default
	public String getKey(){
		return requireId();
	}
}