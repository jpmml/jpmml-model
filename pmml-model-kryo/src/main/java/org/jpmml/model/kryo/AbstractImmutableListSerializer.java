/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Serializer;
import org.jpmml.model.collections.AbstractImmutableList;

abstract
public class AbstractImmutableListSerializer<E extends AbstractImmutableList<?>> extends Serializer<E> {

	public AbstractImmutableListSerializer(){
		super(false, true);
	}
}