/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValueUtilTest {

	@Test
	public void checkDecimalRange(){
		BigDecimal min = DecimalAdapter.MIN_VALUE;
		BigDecimal max = DecimalAdapter.MAX_VALUE;

		assertTrue(ValueUtil.checkRange(BigDecimal.ZERO, min, max));

		assertTrue(ValueUtil.checkRange(min, min, max));
		assertFalse(ValueUtil.checkRange(min.subtract(BigDecimal.ONE), min, max));

		assertTrue(ValueUtil.checkRange(max, min, max));
		assertFalse(ValueUtil.checkRange(max.add(BigDecimal.ONE), min, max));
	}

	@Test
	public void checkIntegerRange(){
		BigInteger min = IntegerAdapter.MIN_VALUE;
		BigInteger max = IntegerAdapter.MAX_VALUE;

		assertTrue(ValueUtil.checkRange(BigInteger.ZERO, min, max));

		assertTrue(ValueUtil.checkRange(min, min, max));
		assertFalse(ValueUtil.checkRange(min.subtract(BigInteger.ONE), min, max));

		assertTrue(ValueUtil.checkRange(max, min, max));
		assertFalse(ValueUtil.checkRange(max.add(BigInteger.ONE), min, max));
	}
}