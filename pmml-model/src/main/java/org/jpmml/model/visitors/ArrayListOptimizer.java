/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.visitors.AbstractSimpleVisitor;

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

			Object value;

			try {
				value = field.get(object);
			} catch(IllegalAccessException iae){
				throw new RuntimeException(iae);
			}

			if(!(value instanceof ArrayList)){
				continue;
			}

			ArrayList<?> list = (ArrayList<?>)value;

			list.trimToSize();
		}

		return VisitorAction.CONTINUE;
	}
}