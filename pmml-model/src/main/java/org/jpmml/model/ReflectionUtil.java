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
import java.util.function.Predicate;

import org.dmg.pmml.PMMLObject;

public class ReflectionUtil {

	private ReflectionUtil(){
	}

	static
	public <E extends PMMLObject> void copyState(E from, E to){
		Class<?> fromClazz = from.getClass();
		Class<?> toClazz = to.getClass();

		// Allow copying to the same class or to a subclass, but not to a superclass
		if(!(fromClazz).isAssignableFrom(toClazz)){
			throw new IllegalArgumentException();
		}

		List<Field> fields = getInstanceFields(fromClazz);
		for(Field field : fields){
			Object value = getFieldValue(field, from);

			setFieldValue(field, to, value);
		}
	}

	static
	public Field getField(Class<?> clazz, String name){

		while(clazz != null){

			try {
				return clazz.getDeclaredField(name);
			} catch(NoSuchFieldException nsfe){
				// Ignored
			}

			clazz = clazz.getSuperclass();
		}

		throw new RuntimeException(new NoSuchFieldException(name));
	}

	static
	public List<Field> getFields(Class<?> clazz){
		List<Field> result = ReflectionUtil.classFields.get(clazz);

		if(result == null){
			result = loadFields(clazz, ReflectionUtil.FIELD_SELECTOR);

			ReflectionUtil.classFields.putIfAbsent(clazz, result);
		}

		return result;
	}

	static
	public List<Field> getInstanceFields(Class<?> clazz){
		List<Field> result = ReflectionUtil.classInstanceFields.get(clazz);

		if(result == null){
			result = loadFields(clazz, ReflectionUtil.INSTANCE_FIELD_SELECTOR);

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
	public boolean isPrimitiveWrapper(Class<?> clazz){
		return ReflectionUtil.primitiveWrapperClasses.contains(clazz);
	}

	static
	public boolean isDefaultValue(Object value){

		if(value instanceof Boolean){
			return (Boolean.FALSE).equals(value);
		} else

		if(value instanceof Character){
			Character character = (Character)value;

			return character.charValue() == '\u0000';
		} else

		if(value instanceof Number){
			Number number = (Number)value;

			return Double.compare(number.doubleValue(), 0d) == 0;
		}

		return false;
	}

	static
	private List<Field> loadFields(Class<?> clazz, Predicate<Field> predicate){
		List<Field> result = new ArrayList<>();

		while(clazz != null){
			Field[] fields = clazz.getDeclaredFields();

			for(Field field : fields){

				if(predicate.test(field)){
					result.add(field);
				}
			}

			clazz = clazz.getSuperclass();
		}

		return Collections.unmodifiableList(result);
	}

	static
	private final Predicate<Field> FIELD_SELECTOR = new Predicate<Field>(){

		@Override
		public boolean test(Field field){
			return hasValidName(field);
		}

		private boolean hasValidName(Field field){
			String name = field.getName();

			if(name.length() > 0){
				return Character.isLetterOrDigit(name.charAt(0));
			}

			return false;
		}
	};

	static
	private final Predicate<Field> INSTANCE_FIELD_SELECTOR = new Predicate<Field>(){

		@Override
		public boolean test(Field field){

			if(ReflectionUtil.FIELD_SELECTOR.test(field)){
				int modifiers = field.getModifiers();

				return !Modifier.isStatic(modifiers);
			}

			return false;
		}
	};

	private static final ConcurrentMap<Class<?>, List<Field>> classFields = new ConcurrentHashMap<>();

	private static final ConcurrentMap<Class<?>, List<Field>> classInstanceFields = new ConcurrentHashMap<>();

	private static final Set<Class<?>> primitiveWrapperClasses = new HashSet<>(Arrays.<Class<?>>asList(Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Character.class));
}