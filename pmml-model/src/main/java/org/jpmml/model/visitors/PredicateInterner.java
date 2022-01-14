/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.False;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.True;
import org.jpmml.model.PMMLObjectCache;

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

	public static final ThreadLocal<PMMLObjectCache<Predicate>> CACHE_PROVIDER = new ThreadLocal<PMMLObjectCache<Predicate>>(){

		@Override
		public PMMLObjectCache<Predicate> initialValue(){
			return new PMMLObjectCache<>();
		}
	};
}