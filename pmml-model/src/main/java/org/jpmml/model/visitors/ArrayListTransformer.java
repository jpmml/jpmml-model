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
 * A Visitor that transforms the {@link List} implementation class of element lists.
 * </p>
 */
public class ArrayListTransformer extends AbstractVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getInstanceFields(object.getClass());

		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(value instanceof ArrayList){
				ArrayList<?> list = (ArrayList<?>)value;

				List<?> transformedList = transform(list);
				if(list != transformedList){
					ReflectionUtil.setFieldValue(field, object, transformedList);
				}
			}
		}

		return super.visit(object);
	}

	public List<?> transform(List<?> list){

		if(list.size() == 0){
			return Collections.emptyList();
		} else

		if(list.size() == 1){
			return new SingletonList<>(list.get(0));
		} else

		if(list.size() == 2){
			return new DoubletonList<>(list.get(0), list.get(1));
		}

		return list;
	}
}