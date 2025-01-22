/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class VersionTest {

	@Test
	public void forNamespaceURI(){

		try {
			Version.forNamespaceURI("http://www.dmg.org/PMML-2_0");

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}

		Version.forNamespaceURI("http://www.dmg.org/PMML-3_0");
		Version.forNamespaceURI("http://www.dmg.org/PMML-3_2");
		Version.forNamespaceURI("http://www.dmg.org/PMML-4_0");
		Version.forNamespaceURI("http://www.dmg.org/PMML-4_3");
		Version.forNamespaceURI("http://www.dmg.org/PMML-4_4");

		try {
			Version.forNamespaceURI("http://www.dmg.org/PMML-4_5");

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}

		try {
			Version.forNamespaceURI("http://www.dmg.org/PMML-5_0");

			fail();
		} catch(IllegalArgumentException iae){
			// Ignored
		}
	}
}