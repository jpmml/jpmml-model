/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

public interface NamespaceURIs {

	String PMML_LATEST = Version.PMML_4_4.getNamespaceURI();
	String PMML_EXTENDED = Version.XPMML.getNamespaceURI();

	String JPMML_INLINETABLE = "http://jpmml.org/jpmml-model/InlineTable";
}