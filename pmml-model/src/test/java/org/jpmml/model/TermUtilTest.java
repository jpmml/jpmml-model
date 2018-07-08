/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TermUtilTest {

	@Test
	public void trimPunctuation(){
		assertEquals("", TermUtil.trimPunctuation(""));

		assertEquals("", TermUtil.trimPunctuation("?"));
		assertEquals("", TermUtil.trimPunctuation("\u00BF?"));

		assertEquals("one", TermUtil.trimPunctuation("one"));
		assertEquals("one", TermUtil.trimPunctuation("one?"));
		assertEquals("one", TermUtil.trimPunctuation("\u00BFone?"));
	}
}