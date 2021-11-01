/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

abstract
public class ReflectionTest {

	static
	public void checkField(Class<?> leftClazz, Class<?> rightClazz, String name) throws NoSuchFieldException {
		Field leftField = leftClazz.getDeclaredField(name);
		Field rightField = rightClazz.getDeclaredField(name);

		assertEquals(leftField.getType(), rightField.getType());
	}
}