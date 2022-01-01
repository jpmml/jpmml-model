/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.CompoundPredicate;
import org.dmg.pmml.CustomSimplePredicate;
import org.dmg.pmml.SimplePredicate;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class StringInternerTest {

	@Test
	public void intern(){
		SimplePredicate left = new CustomSimplePredicate("x", SimplePredicate.Operator.LESS_THAN, new String("0"));

		SimplePredicate right = new CustomSimplePredicate("y", SimplePredicate.Operator.LESS_THAN, new String("0"));

		assertNotSame(left.requireValue(), right.requireValue());

		CompoundPredicate compoundPredicate = new CompoundPredicate(CompoundPredicate.BooleanOperator.OR, null)
			.addPredicates(left, right);

		StringInterner interner = new StringInterner();
		interner.applyTo(compoundPredicate);

		assertSame(left.requireValue(), right.requireValue());
	}
}