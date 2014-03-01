/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;

import org.dmg.pmml.*;

public class SchemaInspector extends AbstractSimpleVisitor {

	private Version minimum = Version.PMML_3_0;

	private Version maximum = Version.PMML_4_2;


	@Override
	public VisitorAction visit(PMMLObject object){
		Class<?> clazz = object.getClass();

		Schema typeSchema = clazz.getAnnotation(Schema.class);
		if(typeSchema != null){
			update(typeSchema);
		}

		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){

			if(!field.isAccessible()){
				field.setAccessible(true);
			}

			Object value;

			try {
				value = field.get(object);
			} catch(IllegalAccessException iae){
				throw new IllegalStateException(iae);
			}

			if(value == null){
				continue;
			}

			Schema fieldSchema = field.getAnnotation(Schema.class);
			if(fieldSchema != null){
				update(fieldSchema);
			}
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