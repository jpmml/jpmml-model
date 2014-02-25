/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.*;

import org.dmg.pmml.*;

public class PMMLUtil {

	private PMMLUtil(){
	}

	static
	public InputStream getResourceAsStream(Version version){
		String name = version.getNamespaceURI();
		name = name.substring(name.lastIndexOf('/') + 1);

		return PMMLUtil.class.getResourceAsStream("/pmml/" + name.toLowerCase() + ".pmml");
	}
}