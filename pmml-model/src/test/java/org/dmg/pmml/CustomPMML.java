/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (
	name = "PMML"
)
public class CustomPMML extends PMML {

	public CustomPMML(){
		super();
	}

	public CustomPMML(String version, Header header, DataDictionary dataDictionary){
		super(version, header, dataDictionary);
	}
}