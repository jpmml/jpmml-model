/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

/**
 * <p>
 * A Visitor that transforms the {@link List} implementation class of empty and singleton element lists.
 * </p>
 */
public class ArrayListTransformer extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getInstanceFields(object.getClass());

		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(value instanceof ArrayList){
				ArrayList<?> list = (ArrayList<?>)value;

				List<?> transformedList;

				if(list.size() == 0){
					transformedList = Collections.emptyList();
				} else

				if(list.size() == 1){
					transformedList = Collections.singletonList(list.get(0));
				} else

				{
					continue;
				}

				ReflectionUtil.setFieldValue(field, object, transformedList);
			}
		}

		return VisitorAction.CONTINUE;
	}
}