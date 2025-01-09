/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class PMMLObjectTest {

	@Test
	public void getSchemaVersion(){
		assertArrayEquals(new int[]{4, 4, 1, 8}, PMMLObject.getSchemaVersion());
	}
}