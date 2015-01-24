/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;

/**
 * A Visitor that optimizes the size of element lists.
 */
public class ArrayListOptimizer extends AbstractSimpleVisitor {

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

				if(value instanceof ArrayList){
					ArrayList<?> list = (ArrayList<?>)value;

					list.trimToSize();
				}
			} catch(IllegalAccessException iae){
				throw new RuntimeException(iae);
			}
		}

		return VisitorAction.CONTINUE;
	}
}