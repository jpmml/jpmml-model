/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class VersionTest {

	@Test
	public void forNamespaceURI(){
		assertThrows(IllegalArgumentException.class, () -> Version.forNamespaceURI("http://www.dmg.org/PMML-2_0"));

		Version.forNamespaceURI("http://www.dmg.org/PMML-3_0");
		Version.forNamespaceURI("http://www.dmg.org/PMML-3_2");
		Version.forNamespaceURI("http://www.dmg.org/PMML-4_0");
		Version.forNamespaceURI("http://www.dmg.org/PMML-4_3");
		Version.forNamespaceURI("http://www.dmg.org/PMML-4_4");

		assertThrows(IllegalArgumentException.class, () -> Version.forNamespaceURI("http://www.dmg.org/PMML-4_5"));
		assertThrows(IllegalArgumentException.class, () -> Version.forNamespaceURI("http://www.dmg.org/PMML-5_0"));
	}
}