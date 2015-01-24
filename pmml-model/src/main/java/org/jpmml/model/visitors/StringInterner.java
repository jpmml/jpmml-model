/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

/**
 * A Visitor that interns String attribute values.
 */
public class StringInterner extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getAllFields(object);

		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(value instanceof String){
				String string = (String)value;

				ReflectionUtil.setFieldValue(field, object, intern(string));
			}
		}

		return VisitorAction.CONTINUE;
	}

	public String intern(String string){
		return string.intern();
	}
}