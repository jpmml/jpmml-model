/*
 * Copyright (c) 2012 University of Tartu
 */
package org.jpmml.model;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayUtilTest {

	@Test
	public void parseNumberArray(){
		assertEquals(Arrays.asList("1"), parseNumberArray("1"));
		assertEquals(Arrays.asList("1.0"), parseNumberArray("1.0"));

		assertEquals(Arrays.asList("1", "2", "3"), parseNumberArray("1 2 3"));
		assertEquals(Arrays.asList("1.0", "2.0", "3.0"), parseNumberArray("1.0 2.0 3.0"));
	}

	@Test
	public void parseStringArray(){
		assertEquals(Arrays.asList("a"), parseStringArray("\"a\""));

		assertEquals(Arrays.asList("a", "b", "c"), parseStringArray("a b c"));
		assertEquals(Arrays.asList("a", "b", "c"), parseStringArray("\"a\" \"b\" \"c\""));

		assertEquals(Arrays.asList("a b c"), parseStringArray("\"a b c\""));

		assertEquals(Arrays.asList("\"a b c"), parseStringArray("\"a b c"));
		assertEquals(Arrays.asList("\\a", "\\b\\", "c\\"), parseStringArray("\\a \\b\\ c\\"));

		assertEquals(Arrays.asList("a \"b\" c"), parseStringArray("\"a \\\"b\\\" c\""));
		assertEquals(Arrays.asList("\"a b c\""), parseStringArray("\"\\\"a b c\\\"\""));
	}

	static
	private List<String> parseNumberArray(String string){
		return ArrayUtil.parse(string, false);
	}

	static
	private List<String> parseStringArray(String string){
		return ArrayUtil.parse(string, true);
	}
}