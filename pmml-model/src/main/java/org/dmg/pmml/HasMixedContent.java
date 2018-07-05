/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

public interface HasMixedContent<E extends PMMLObject & HasMixedContent<E>> {

	boolean hasContent();

	List<Object> getContent();

	E addContent(Object... objects);
}