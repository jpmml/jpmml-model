/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

public enum Version {
	PMML_3_0("http://www.dmg.org/PMML-3_0"),
	PMML_3_1("http://www.dmg.org/PMML-3_1"),
	PMML_3_2("http://www.dmg.org/PMML-3_2"),
	PMML_4_0("http://www.dmg.org/PMML-4_0"),
	PMML_4_1("http://www.dmg.org/PMML-4_1"),
	PMML_4_2("http://www.dmg.org/PMML-4_2"),
	PMML_4_3("http://www.dmg.org/PMML-4_3"),
	PMML_4_4("http://www.dmg.org/PMML-4_4"),

	/**
	 * Extended PMML
	 */
	XPMML("http://xpmml.org/XPMML"){

		@Override
		public String getVersion(){
			throw new UnsupportedOperationException();
		}
	}
	;

	private String namespaceURI = null;


	private Version(String namespaceURI){
		setNamespaceURI(namespaceURI);
	}

	public boolean isStandard(){
		String namespaceURI = getNamespaceURI();

		return namespaceURI.matches(Version.REGEX_PMML_XMLNS);
	}

	public String getNamespaceURI(){
		return this.namespaceURI;
	}

	private void setNamespaceURI(String namespaceURI){
		this.namespaceURI = namespaceURI;
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

		return versions[(versions.length - 1) - 1];
	}

	static
	public Version forNamespaceURI(String namespaceURI){
		Version[] versions = Version.values();

		for(Version version : versions){

			if((version.getNamespaceURI()).equals(namespaceURI)){
				return version;
			}
		}

		boolean valid = (namespaceURI != null && namespaceURI.matches(Version.REGEX_PMML_XMLNS));
		if(!valid){
			throw new IllegalArgumentException("PMML namespace URI " + namespaceURI + " does not match \'" + Version.REGEX_PMML_XMLNS + "\' regex pattern");
		}

		throw new IllegalArgumentException("PMML namespace URI " + namespaceURI + " is not supported");
	}

	private static final String REGEX_PMML_XMLNS = "http://www\\.dmg\\.org/PMML\\-\\d_\\d";
}