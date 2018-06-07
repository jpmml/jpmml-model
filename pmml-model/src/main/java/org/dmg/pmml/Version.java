/*
 * Copyright (c) 2012 University of Tartu
 */
package org.dmg.pmml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Version {
	PMML_3_0("http://www.dmg.org/PMML-3_0"),
	PMML_3_1("http://www.dmg.org/PMML-3_1"),
	PMML_3_2("http://www.dmg.org/PMML-3_2"),
	PMML_4_0("http://www.dmg.org/PMML-4_0"),
	PMML_4_1("http://www.dmg.org/PMML-4_1"),
	PMML_4_2("http://www.dmg.org/PMML-4_2"),
	PMML_4_3("http://www.dmg.org/PMML-4_3"),
	;

	private String namespaceUri = null;


	private Version(String namespaceUri){
		setNamespaceURI(namespaceUri);
	}

	public String getNamespaceURI(){
		return this.namespaceUri;
	}

	private void setNamespaceURI(String namespaceUri){
		this.namespaceUri = namespaceUri;
	}

	public String getVersion(){
		String namespaceURI = getNamespaceURI();

		String version = namespaceURI.substring("http://www.dmg.org/PMML-".length());

		return version.replace('_', '.');
	}

	static
	public Version getMinimum(){
		Version[] versions = Version.values();

		return versions[0];
	}

	static
	public Version getMaximum(){
		Version[] versions = Version.values();

		return versions[versions.length - 1];
	}

	static
	public Version forNamespaceURI(String namespaceURI){
		Version[] versions = Version.values();

		for(Version version : versions){

			if((version.getNamespaceURI()).equals(namespaceURI)){
				return version;
			}
		}

		Matcher matcher = Version.PATTERN_XMLNS.matcher(namespaceURI);
		if(!matcher.matches()){
			throw new IllegalArgumentException("PMML namespace URI " + namespaceURI + " does not match \'" + Version.PATTERN_XMLNS.pattern() + "\' regex pattern");
		}

		throw new IllegalArgumentException("PMML namespace URI " + namespaceURI + " is not supported");
	}

	private static final Pattern PATTERN_XMLNS = Pattern.compile("http://www\\.dmg\\.org/PMML\\-\\d_\\d");
}