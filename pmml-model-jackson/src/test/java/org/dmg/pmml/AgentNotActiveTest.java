/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @see AgentActiveTest
 */
public class AgentNotActiveTest {

	@Test
	public void accessLocator() throws Exception {
		Field locatorField = PMMLObject.class.getDeclaredField("locator");

		assertFalse(Modifier.isPublic(locatorField.getModifiers()));
	}
}