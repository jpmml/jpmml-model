/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.MiningField;
import org.jpmml.model.PMMLObjectCache;

/**
 * <p>
 * A Visitor that interns {@link MiningField} elements.
 * </p>
 */
public class MiningFieldInterner extends PMMLObjectInterner<MiningField> {

	public MiningFieldInterner(){
		super(MiningField.class, MiningFieldInterner.CACHE_PROVIDER.get());
	}

	public static final ThreadLocal<PMMLObjectCache<MiningField>> CACHE_PROVIDER = new ThreadLocal<PMMLObjectCache<MiningField>>(){

		@Override
		public PMMLObjectCache<MiningField> initialValue(){
			return new PMMLObjectCache<>();
		}
	};
}