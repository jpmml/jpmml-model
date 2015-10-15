/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasExpression {

	Expression getExpression();

	HasExpression setExpression(Expression expression);
}