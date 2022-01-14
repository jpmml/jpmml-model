/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlEnumValue;
import org.dmg.pmml.PMMLObject;

public class EnumUtil {

	private EnumUtil(){
	}

	static
	public Field getEnumField(PMMLObject object, Enum<?> value){
		Class<?> clazz = object.getClass();

		while(clazz != null){
			Field[] fields = clazz.getDeclaredFields();

			for(Field field : fields){

				if(Objects.equals(field.getType(), value.getClass())){
					return field;
				}
			}

			clazz = clazz.getSuperclass();
		}

		throw new IllegalArgumentException();
	}

	static
	public String getEnumValue(Enum<?> value){
		Class<?> clazz = value.getClass();

		Field field;

		try {
			field = clazz.getField(value.name());
		} catch(NoSuchFieldException nsfe){
			throw new RuntimeException(nsfe);
		}

		XmlEnumValue enumValue = field.getAnnotation(XmlEnumValue.class);
		if(enumValue != null){
			return enumValue.value();
		}

		throw new IllegalArgumentException();
	}
}