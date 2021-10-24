/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlRootElement;
import org.jpmml.model.ReflectionUtil;

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

	public CustomPMML(PMML pmml){
		super();

		ReflectionUtil.copyState(pmml, this);
	}

	@Override
	public String getBaseVersion(){
		String baseVersion = super.getBaseVersion();

		if(baseVersion == null){
			baseVersion = super.getVersion();
		}

		return baseVersion;
	}
}