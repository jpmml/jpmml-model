/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class PMMLModule extends JaxbAnnotationModule {

	public PMMLModule(){
		super();
	}

	public PMMLModule(JaxbAnnotationIntrospector annotationIntrospector){
		super(annotationIntrospector);
	}

	@Override
	public void setupModule(SetupContext context){
		super.setupModule(context);
	}
}