/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReflectionUtil {

	private ReflectionUtil(){
	}

	static
	public Field getField(Object object, String name){

		for(Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()){

			try {
				return clazz.getDeclaredField(name);
			} catch(NoSuchFieldException nsfe){
				// Ignored
			}
		}

		throw new RuntimeException(new NoSuchFieldException(name));
	}

	static
	public List<Field> getAllFields(Object object){
		Class<?> key = object.getClass();

		List<Field> result = ReflectionUtil.classFields.get(key);
		if(result == null){
			result = new ArrayList<Field>();

			for(Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()){
				Field[] fields = clazz.getDeclaredFields();

				result.addAll(Arrays.asList(fields));
			}

			result = Collections.unmodifiableList(result);

			ReflectionUtil.classFields.putIfAbsent(key, result);
		}

		return result;
	}

	@SuppressWarnings (
		value = {"unchecked"}
	)
	static
	public <E> E getFieldValue(Field field, Object object){

		if(!field.isAccessible()){
			field.setAccessible(true);
		}

		try {
			return (E)field.get(object);
		} catch(IllegalAccessException iae){
			throw new RuntimeException(iae);
		}
	}

	static
	public void setFieldValue(Field field, Object object, Object value){

		if(!field.isAccessible()){
			field.setAccessible(true);
		}

		try {
			field.set(object, value);
		} catch(IllegalAccessException iae){
			throw new RuntimeException(iae);
		}
	}

	private static final ConcurrentMap<Class<?>, List<Field>> classFields = new ConcurrentHashMap<Class<?>, List<Field>>();
}