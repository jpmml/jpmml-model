/*
 * Copyright (c) 2012 University of Tartu
 */
package org.jpmml.schema;

public enum Version {
	PMML_3_0("http://www.dmg.org/PMML-3_0"),
	PMML_3_1("http://www.dmg.org/PMML-3_1"),
	PMML_3_2("http://www.dmg.org/PMML-3_2"),
	PMML_4_0("http://www.dmg.org/PMML-4_0"),
	PMML_4_1("http://www.dmg.org/PMML-4_1"),
	PMML_4_2("http://www.dmg.org/PMML-4_2"),
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

		return (namespaceURI.substring("http://www.dmg.org/PMML-".length())).replace('_', '.');
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

		throw new IllegalArgumentException(namespaceURI);
	}
}