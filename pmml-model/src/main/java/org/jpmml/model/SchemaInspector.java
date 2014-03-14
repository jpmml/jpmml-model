/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.*;
import java.lang.reflect.Field;

import org.dmg.pmml.*;

public class SchemaInspector extends AbstractSimpleVisitor {

	private Version minimum = Version.PMML_3_0;

	private Version maximum = Version.PMML_4_2;


	@Override
	public VisitorAction visit(PMMLObject object){
		Class<?> clazz = object.getClass();
		update(clazz);

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

			update(field);

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

				update(enumField);
			}
		}

		return VisitorAction.CONTINUE;
	}

	private void update(AnnotatedElement element){
		Added added = element.getAnnotation(Added.class);
		if(added != null){
			Version minimum = added.value();

			if(minimum != null && minimum.compareTo(this.minimum) > 0){
				this.minimum = minimum;
			}
		}

		Removed removed = element.getAnnotation(Removed.class);
		if(removed != null){
			Version maximum = removed.value();

			if(maximum != null && maximum.compareTo(this.maximum) < 0){
				this.maximum = maximum;
			}
		}
	}

	public Version getMinimum(){
		return this.minimum;
	}

	public Version getMaximum(){
		return this.maximum;
	}
}