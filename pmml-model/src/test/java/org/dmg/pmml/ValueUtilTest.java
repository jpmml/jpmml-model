/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import java.math.BigInteger;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValueUtilTest {

	@Test
	public void checkRange(){
		BigInteger min = BigInteger.valueOf(Integer.MIN_VALUE);
		BigInteger max = BigInteger.valueOf(Integer.MAX_VALUE);

		assertTrue(ValueUtil.checkRange(BigInteger.ZERO, min, max));

		assertTrue(ValueUtil.checkRange(min, min, max));
		assertFalse(ValueUtil.checkRange(min.subtract(BigInteger.ONE), min, max));

		assertTrue(ValueUtil.checkRange(max, min, max));
		assertFalse(ValueUtil.checkRange(max.add(BigInteger.ONE), min, max));
	}
}