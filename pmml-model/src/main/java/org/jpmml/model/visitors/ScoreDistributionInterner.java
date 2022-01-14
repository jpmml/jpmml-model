/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dmg.pmml.PMMLObjectKey;
import org.dmg.pmml.ScoreDistribution;

/**
 * <p>
 * A Visitor that interns {@link ScoreDistribution} elements.
 * </p>
 */
public class ScoreDistributionInterner extends PMMLObjectInterner<ScoreDistribution> {

	public ScoreDistributionInterner(){
		super(ScoreDistribution.class, ScoreDistributionInterner.CACHE_PROVIDER.get());
	}

	static
	public void clear(){
		ScoreDistributionInterner.cache.clear();
	}

	private static final ConcurrentMap<PMMLObjectKey, ScoreDistribution> cache = new ConcurrentHashMap<>();

	public static final ThreadLocal<ConcurrentMap<PMMLObjectKey, ScoreDistribution>> CACHE_PROVIDER = new ThreadLocal<ConcurrentMap<PMMLObjectKey, ScoreDistribution>>(){

		@Override
		public ConcurrentMap<PMMLObjectKey, ScoreDistribution> initialValue(){
			return ScoreDistributionInterner.cache;
		}
	};
}