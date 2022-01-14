/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dmg.pmml.False;
import org.dmg.pmml.PMMLObjectKey;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.True;

/**
 * <p>
 * A Visitor that interns {@link Predicate} elements.
 * </p>
 */
public class PredicateInterner extends PMMLObjectInterner<Predicate> {

	public PredicateInterner(){
		super(Predicate.class, PredicateInterner.CACHE_PROVIDER.get());
	}

	@Override
	public Predicate intern(Predicate predicate){

		if(predicate instanceof True){
			True truePredicate = (True)predicate;

			if(!truePredicate.hasExtensions()){
				return True.INSTANCE;
			}
		} else

		if(predicate instanceof False){
			False falsePredicate = (False)predicate;

			if(!falsePredicate.hasExtensions()){
				return False.INSTANCE;
			}
		}

		return super.intern(predicate);
	}

	static
	public void clear(){
		PredicateInterner.cache.clear();
	}

	private static final ConcurrentMap<PMMLObjectKey, Predicate> cache = new ConcurrentHashMap<>();

	public static final ThreadLocal<ConcurrentMap<PMMLObjectKey, Predicate>> CACHE_PROVIDER = new ThreadLocal<ConcurrentMap<PMMLObjectKey, Predicate>>(){

		@Override
		public ConcurrentMap<PMMLObjectKey, Predicate> initialValue(){
			return PredicateInterner.cache;
		}
	};
}