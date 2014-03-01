/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;

import org.dmg.pmml.*;

public class SchemaInspector extends AbstractSimpleVisitor {

	private Version minimum = Version.PMML_3_0;

	private Version maximum = Version.PMML_4_2;


	@Override
	public VisitorAction visit(PMMLObject object){
		Class<?> clazz = object.getClass();

		Schema typeSchema = clazz.getAnnotation(Schema.class);
		if(typeSchema != null){
			update(typeSchema);
		}

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

			Schema fieldSchema = field.getAnnotation(Schema.class);
			if(fieldSchema != null){
				update(fieldSchema);
			} // End if

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

				Schema enumConstantSchema = enumField.getAnnotation(Schema.class);
				if(enumConstantSchema != null){
					update(enumConstantSchema);
				}
			}
		}

		return VisitorAction.CONTINUE;
	}

	private void update(Schema schema){
		Version minimum = schema.min();
		if(minimum != null && minimum.compareTo(this.minimum) > 0){
			this.minimum = minimum;
		}

		Version maximum = schema.max();
		if(maximum != null && maximum.compareTo(this.maximum) < 0){
			this.maximum = maximum;
		}
	}

	public Version getMinimum(){
		return this.minimum;
	}

	public Version getMaximum(){
		return this.maximum;
	}
}