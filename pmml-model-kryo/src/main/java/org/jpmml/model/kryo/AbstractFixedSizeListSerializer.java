/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Serializer;
import org.jpmml.model.collections.AbstractFixedSizeList;

abstract
public class AbstractFixedSizeListSerializer<E extends AbstractFixedSizeList<?>> extends Serializer<E> {

	public AbstractFixedSizeListSerializer(){
		super(false, true);
	}
}