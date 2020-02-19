/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class PMMLModule extends SimpleModule {

	public PMMLModule(){
		super("PMML");
	}
}