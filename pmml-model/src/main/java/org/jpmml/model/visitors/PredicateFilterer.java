/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;
import java.util.ListIterator;

import org.dmg.pmml.CompoundPredicate;
import org.dmg.pmml.HasPredicate;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.VisitorAction;

/**
 * <p>
 * This class provides a skeletal implementation of {@link Predicate} filterers.
 * </p>
 */
abstract
public class PredicateFilterer extends AbstractVisitor {

	abstract
	public Predicate filter(Predicate predicate);

	public void filterAll(List<Predicate> predicates){

		for(ListIterator<Predicate> it = predicates.listIterator(); it.hasNext(); ){
			it.set(filter(it.next()));
		}
	}

	@Override
	public VisitorAction visit(PMMLObject object){

		if(object instanceof HasPredicate){
			HasPredicate<?> hasPredicate = (HasPredicate<?>)object;

			hasPredicate.setPredicate(filter(hasPredicate.getPredicate()));
		}

		return super.visit(object);
	}

	@Override
	public VisitorAction visit(CompoundPredicate compoundPredicate){

		if(compoundPredicate.hasPredicates()){
			filterAll(compoundPredicate.getPredicates());
		}

		return super.visit(compoundPredicate);
	}
}