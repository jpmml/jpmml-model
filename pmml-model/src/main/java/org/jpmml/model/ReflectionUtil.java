/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
		Class<?> clazz = object.getClass();

		List<Field> result = ReflectionUtil.classFields.get(clazz);
		if(result == null){
			FieldFilter filter = new FieldFilter(){

				@Override
				public boolean accept(Field field){
					return true;
				}
			};

			result = loadFields(clazz, filter);

			ReflectionUtil.classFields.putIfAbsent(clazz, result);
		}

		return result;
	}

	static
	public List<Field> getAllInstanceFields(Object object){
		Class<?> clazz = object.getClass();

		List<Field> result = ReflectionUtil.classInstanceFields.get(clazz);
		if(result == null){
			FieldFilter filter = new FieldFilter(){

				@Override
				public boolean accept(Field field){
					int modifiers = field.getModifiers();

					return !Modifier.isStatic(modifiers);
				}
			};

			result = loadFields(clazz, filter);

			ReflectionUtil.classInstanceFields.putIfAbsent(clazz, result);
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

	static
	public boolean isPrimitiveWrapper(Object object){
		Class<?> clazz = object.getClass();

		return ReflectionUtil.primitiveWrapperClasses.contains(clazz);
	}

	static
	public boolean isEnum(Object object){
		Class<?> clazz = object.getClass();

		return clazz.isEnum();
	}

	static
	public boolean isDefaultValue(Object value){

		if(value instanceof Boolean){
			return (Boolean.FALSE).equals(value);
		} else

		if(value instanceof Character){
			Character character = (Character)value;

			return Character.compare(character.charValue(), '\u0000') == 0;
		} else

		if(value instanceof Number){
			Number number = (Number)value;

			return Double.compare(number.doubleValue(), 0d) == 0;
		}

		return false;
	}

	static
	private List<Field> loadFields(Class<?> clazz, FieldFilter filter){
		List<Field> result = new ArrayList<Field>();

		while(clazz != null){
			Field[] fields = clazz.getDeclaredFields();

			for(Field field : fields){

				if(filter.accept(field)){
					result.add(field);
				}
			}

			clazz = clazz.getSuperclass();
		}

		return Collections.unmodifiableList(result);
	}

	static
	private interface FieldFilter {

		boolean accept(Field field);
	}

	private static final ConcurrentMap<Class<?>, List<Field>> classFields = new ConcurrentHashMap<Class<?>, List<Field>>();

	private static final ConcurrentMap<Class<?>, List<Field>> classInstanceFields = new ConcurrentHashMap<Class<?>, List<Field>>();

	private static final Set<Class<?>> primitiveWrapperClasses = new HashSet<Class<?>>(Arrays.<Class<?>>asList(Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Character.class));
}