/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.*;

import org.jpmml.schema.*;

public class SchemaInspector extends AnnotationInspector {

	private Version minimum = Version.PMML_3_0;

	private Version maximum = Version.PMML_4_2;


	@Override
	public void inspect(AnnotatedElement element){
		Added added = element.getAnnotation(Added.class);
		if(added != null){
			updateMinimum(added.value());
		}

		Removed removed = element.getAnnotation(Removed.class);
		if(removed != null){
			updateMaximum(removed.value());
		}
	}

	public Version getMinimum(){
		return this.minimum;
	}

	private void updateMinimum(Version minimum){

		if(minimum != null && minimum.compareTo(this.minimum) > 0){
			this.minimum = minimum;
		}
	}

	public Version getMaximum(){
		return this.maximum;
	}

	private void updateMaximum(Version maximum){

		if(maximum != null && maximum.compareTo(this.maximum) < 0){
			this.maximum = maximum;
		}
	}
}