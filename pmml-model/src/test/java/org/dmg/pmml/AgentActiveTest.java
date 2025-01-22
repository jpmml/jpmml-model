/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentActiveTest {

	@Test
	public void accessLocator() throws Exception {
		Field locatorField = PMMLObject.class.getDeclaredField("locator");

		assertTrue(Modifier.isPublic(locatorField.getModifiers()));
	}

	@Test
	public void accessObjectState() throws Exception {
		Field[] fields = PMML.class.getDeclaredFields();

		for(Field field : fields){
			int modifiers = field.getModifiers();

			if(Modifier.isStatic(modifiers)){
				continue;
			}

			assertTrue(Modifier.isPublic(modifiers));
		}
	}
}