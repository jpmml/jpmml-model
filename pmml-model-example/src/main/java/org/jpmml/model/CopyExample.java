/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.*;

public class CopyExample extends TransformationExample {

	static
	public void main(String... args) throws Exception {
		execute(CopyExample.class, args);
	}

	@Override
	public PMML transform(PMML pmml){
		return pmml;
	}
}