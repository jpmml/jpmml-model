/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;

/**
 * A Visitor that interns String attribute values.
 */
public class StringInterner extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		Class<?> clazz = object.getClass();

		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){

			if(!field.isAccessible()){
				field.setAccessible(true);
			}

			Object value;

			try {
				value = field.get(object);
			} catch(IllegalAccessException iae){
				throw new RuntimeException(iae);
			}

			if(!(value instanceof String)){
				continue;
			}

			value = intern((String)value);

			try {
				field.set(object, value);
			} catch(IllegalAccessException iae){
				throw new RuntimeException(iae);
			}
		}

		return VisitorAction.CONTINUE;
	}

	public String intern(String string){
		return string.intern();
	}
}