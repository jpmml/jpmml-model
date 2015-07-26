/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

/**
 * <p>
 * A Visitor that optimizes the size of element lists.
 * </p>
 */
public class ArrayListOptimizer extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getAllInstanceFields(object);

		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(value instanceof ArrayList){
				ArrayList<?> list = (ArrayList<?>)value;

				list.trimToSize();
			}
		}

		return VisitorAction.CONTINUE;
	}
}