/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.ScoreDistribution;
import org.jpmml.model.PMMLObjectCache;

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

	private static final PMMLObjectCache<ScoreDistribution> cache = new PMMLObjectCache<>();

	public static final ThreadLocal<PMMLObjectCache<ScoreDistribution>> CACHE_PROVIDER = new ThreadLocal<PMMLObjectCache<ScoreDistribution>>(){

		@Override
		public PMMLObjectCache<ScoreDistribution> initialValue(){
			return ScoreDistributionInterner.cache;
		}
	};
}