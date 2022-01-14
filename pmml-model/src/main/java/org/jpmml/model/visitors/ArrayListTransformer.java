/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.collections.DoubletonList;
import org.jpmml.model.collections.SingletonList;
import org.jpmml.model.collections.TripletonList;

/**
 * <p>
 * A Visitor that transforms the {@link List} implementation class of element lists.
 * </p>
 */
public class ArrayListTransformer extends AbstractVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getFields(object.getClass());

		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if((value != null) && Objects.equals(ArrayList.class, value.getClass())){
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
		int size = list.size();

		switch(size){
			case 0:
				return Collections.emptyList();
			case 1:
				return new SingletonList<>(list.get(0));
			case 2:
				return new DoubletonList<>(list.get(0), list.get(1));
			case 3:
				return new TripletonList<>(list.get(0), list.get(1), list.get(2));
			default:
				return list;
		}
	}
}