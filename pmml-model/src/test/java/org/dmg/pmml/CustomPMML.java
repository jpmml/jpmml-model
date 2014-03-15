/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.*;

@XmlRootElement (
	name = "PMML"
)
public class CustomPMML extends PMML {

	public CustomPMML(){
		super();
	}

	public CustomPMML(Header header, DataDictionary dataDictionary, String version){
		super(header, dataDictionary, version);
	}
}