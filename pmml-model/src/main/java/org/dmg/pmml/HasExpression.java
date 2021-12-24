/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasExpression<E extends PMMLObject & HasExpression<E>> {

	Expression requireExpression();

	Expression getExpression();

	E setExpression(Expression expression);
}