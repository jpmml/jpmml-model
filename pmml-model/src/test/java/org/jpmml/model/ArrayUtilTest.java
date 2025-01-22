/*
 * Copyright (c) 2012 University of Tartu
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.Arrays;
import java.util.Collections;

import org.dmg.pmml.Array;
import org.dmg.pmml.ComplexValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayUtilTest {

	@Test
	public void parseNumberArray(){
		assertEquals(Arrays.asList("1"), ArrayUtil.parse(Array.Type.INT, "1"));
		assertEquals(Arrays.asList("1.0"), ArrayUtil.parse(Array.Type.REAL, "1.0"));

		assertEquals(Arrays.asList("1", "2", "3"), ArrayUtil.parse(Array.Type.INT, "1 2 3"));
		assertEquals(Arrays.asList("1.0", "2.0", "3.0"), ArrayUtil.parse(Array.Type.REAL, "1.0 2.0 3.0"));

		assertEquals(Arrays.asList("1", "22", "3"), ArrayUtil.parse(Array.Type.INT, "1 22 3"));
	}

	@Test
	public void formatNumberArray(){
		ComplexValue integerTwoWrapper = new ComplexValue(){

			@Override
			public Object toSimpleValue(){
				return 2;
			}
		};

		assertEquals("1 2 3", ArrayUtil.format(Array.Type.INT, Arrays.asList(1, integerTwoWrapper, 3)));
		assertEquals("1.0 2.0 3.0", ArrayUtil.format(Array.Type.REAL, Arrays.asList(1d, 2d, 3d)));

		assertEquals("1 22 3", ArrayUtil.format(Array.Type.INT, Arrays.asList("1", "22", "3")));
	}

	@Test
	public void parseStringArray(){
		assertEquals(Collections.emptyList(), ArrayUtil.parse(Array.Type.STRING, ""));

		assertEquals(Arrays.asList(""), ArrayUtil.parse(Array.Type.STRING, "\"\""));
		assertEquals(Arrays.asList("", " "), ArrayUtil.parse(Array.Type.STRING, "\"\" \" \""));

		assertEquals(Arrays.asList("a"), ArrayUtil.parse(Array.Type.STRING, "\"a\""));

		assertEquals(Arrays.asList("a", "b", "c"), ArrayUtil.parse(Array.Type.STRING, "a b c"));
		assertEquals(Arrays.asList("a", "", "b", "c"), ArrayUtil.parse(Array.Type.STRING, "a \"\" b c"));

		assertEquals(Arrays.asList("a", "b", "c"), ArrayUtil.parse(Array.Type.STRING, "\"a\" \"b\" \"c\""));
		assertEquals(Arrays.asList("a", "", "b", "c"), ArrayUtil.parse(Array.Type.STRING, "\"a\" \"\" \"b\" \"c\""));

		assertEquals(Arrays.asList("a b c"), ArrayUtil.parse(Array.Type.STRING, "\"a b c\""));

		assertEquals(Arrays.asList("\"a b c"), ArrayUtil.parse(Array.Type.STRING, "\"a b c"));
		assertEquals(Arrays.asList("\\a", "\\b\\", "c\\"), ArrayUtil.parse(Array.Type.STRING, "\\a \\b\\ c\\"));

		assertEquals(Arrays.asList("a \"b\" c"), ArrayUtil.parse(Array.Type.STRING, "\"a \\\"b\\\" c\""));
		assertEquals(Arrays.asList("\"a b c\""), ArrayUtil.parse(Array.Type.STRING, "\"\\\"a b c\\\"\""));

		assertEquals(Arrays.asList("ab", "a b", "with \"quotes\" "), ArrayUtil.parse(Array.Type.STRING, "ab  \"a b\"   \"with \\\"quotes\\\" \""));
	}

	@Test
	public void formatStringArray(){
		assertEquals("", ArrayUtil.format(Array.Type.STRING, Collections.emptyList()));

		assertEquals("\"\"", ArrayUtil.format(Array.Type.STRING, Arrays.asList("")));
		assertEquals("\"\" \" \"", ArrayUtil.format(Array.Type.STRING, Arrays.asList("", " ")));

		assertEquals("a b c", ArrayUtil.format(Array.Type.STRING, Arrays.asList("a", "b", "c")));
		assertEquals("a \"\" b c", ArrayUtil.format(Array.Type.STRING, Arrays.asList("a", "", "b", "c")));

		assertEquals("\"a b c\"", ArrayUtil.format(Array.Type.STRING, Arrays.asList("a b c")));

		assertEquals("\\a \\b\\ c\\", ArrayUtil.format(Array.Type.STRING, Arrays.asList("\\a", "\\b\\", "c\\")));

		assertEquals("ab \"a b\" \"with \\\"quotes\\\" \"", ArrayUtil.format(Array.Type.STRING, Arrays.asList("ab", "a b", "with \"quotes\" ")));
	}
}