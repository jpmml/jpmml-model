/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.*;

public class SchemaInspector extends AbstractSimpleVisitor {

	private Version minimum = Version.PMML_3_0;

	private Version maximum = Version.PMML_4_2;


	@Override
	public VisitorAction visit(PMMLObject object){
		Class<?> clazz = object.getClass();

		Schema schema = clazz.getAnnotation(Schema.class);
		if(schema != null){
			update(schema);
		}

		return VisitorAction.CONTINUE;
	}

	private void update(Schema schema){
		Version minimum = schema.min();
		if(minimum != null && minimum.compareTo(this.minimum) > 0){
			this.minimum = minimum;
		}

		Version maximum = schema.max();
		if(maximum != null && maximum.compareTo(this.maximum) < 0){
			this.maximum = maximum;
		}
	}

	public Version getMinimum(){
		return this.minimum;
	}

	public Version getMaximum(){
		return this.maximum;
	}
}