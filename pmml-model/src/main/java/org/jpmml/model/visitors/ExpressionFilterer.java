/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;
import java.util.ListIterator;

import org.dmg.pmml.Apply;
import org.dmg.pmml.Expression;
import org.dmg.pmml.HasExpression;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;

/**
 * <p>
 * This class provides a skeletal implementation of {@link Expression} filterers.
 * </p>
 */
abstract
public class ExpressionFilterer extends AbstractVisitor {

	abstract
	public Expression filter(Expression expression);

	public void filterAll(List<Expression> expressions){

		for(ListIterator<Expression> it = expressions.listIterator(); it.hasNext(); ){
			it.set(filter(it.next()));
		}
	}

	@Override
	public VisitorAction visit(PMMLObject object){

		if(object instanceof HasExpression){
			HasExpression<?> hasExpression = (HasExpression<?>)object;

			hasExpression.setExpression(filter(hasExpression.getExpression()));
		}

		return super.visit(object);
	}

	@Override
	public VisitorAction visit(Apply apply){

		if(apply.hasExpressions()){
			filterAll(apply.getExpressions());
		}

		return super.visit(apply);
	}
}