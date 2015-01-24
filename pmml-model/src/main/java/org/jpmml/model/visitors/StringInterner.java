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

			try {
				Object value = field.get(object);

				if(value instanceof String){
					String string = (String)value;

					field.set(object, intern(string));
				}
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