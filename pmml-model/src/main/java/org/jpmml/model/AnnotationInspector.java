/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.*;
import java.lang.reflect.Field;

import org.dmg.pmml.*;

abstract
public class AnnotationInspector extends AbstractSimpleVisitor {

	abstract
	public void inspect(AnnotatedElement element);

	@Override
	public VisitorAction visit(PMMLObject object){
		Class<?> clazz = object.getClass();
		inspect(clazz);

		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			Object value;

			try {
				if(!field.isAccessible()){
					field.setAccessible(true);
				}

				value = field.get(object);
			} catch(IllegalAccessException iae){
				throw new RuntimeException(iae);
			}

			// The field is not set
			if(value == null){
				continue;
			}

			inspect(field);

			// The field is set to an enum constant
			if(value instanceof Enum){
				Enum<?> enumValue = (Enum<?>)value;

				Field enumField;

				try {
					Class<?> enumClazz = enumValue.getClass();

					enumField = enumClazz.getField(enumValue.name());
				} catch(NoSuchFieldException nsfe){
					throw new RuntimeException(nsfe);
				}

				inspect(enumField);
			}
		}

		return VisitorAction.CONTINUE;
	}
}