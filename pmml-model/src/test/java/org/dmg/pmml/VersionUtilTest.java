/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.dmg.pmml;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class VersionUtilTest {

	@Test
	public void compare(){
		assertTrue(VersionUtil.compare("1", "0") > 0);
		assertTrue(VersionUtil.compare("1", "1") == 0);
		assertTrue(VersionUtil.compare("1", "2") < 0);

		assertTrue(VersionUtil.compare("1.1", "1.0") > 0);
		assertTrue(VersionUtil.compare("1.1", "1.1") == 0);
		assertTrue(VersionUtil.compare("1.1", "1.2") < 0);
	}
}