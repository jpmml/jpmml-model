/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class PMMLObjectTest {

	@Test
	public void getSchemaVersion(){
		assertArrayEquals(new int[]{4, 4, 1, 11}, PMMLObject.getSchemaVersion());
	}
}