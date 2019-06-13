/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.dmg.pmml.PMMLObject;

public class ReflectionUtil {

	private ReflectionUtil(){
	}

	static
	public boolean equals(Object left, Object right){

		if(left instanceof PMMLObject && right instanceof PMMLObject){
			return equals((PMMLObject)left, (PMMLObject)right);
		}

		return Objects.equals(left, right);
	}

	static
	public boolean equals(PMMLObject left, PMMLObject right){

		if(!Objects.equals(left.getClass(), right.getClass())){
			throw new IllegalArgumentException();
		}

		Map<Field, Method> getterMethods = getGetterMethods(left.getClass());

		Collection<Map.Entry<Field, Method>> entries = getterMethods.entrySet();
		for(Map.Entry<Field, Method> entry : entries){
			Field field = entry.getKey();
			Method getterMethod = entry.getValue();

			Object leftValue;
			Object rightValue;

			// Avoid getter access, which will initialize previously uninitialized fields with an empty ArrayList instance
			if((List.class).equals(field.getType())){
				leftValue = getFieldValue(field, left);
				rightValue = getFieldValue(field, right);
			} else

			{
				leftValue = getGetterMethodValue(getterMethod, left);
				rightValue = getGetterMethodValue(getterMethod, right);
			}

			leftValue = standardizeValue(leftValue);
			rightValue = standardizeValue(rightValue);

			boolean equals;

			if(leftValue instanceof List && rightValue instanceof List){
				List<?> leftValues = (List<?>)leftValue;
				List<?> rightValues = (List<?>)rightValue;

				if(leftValues.size() == rightValues.size()){
					equals = true;

					for(int i = 0, max = leftValues.size(); i < max; i++){
						equals &= equals(leftValues.get(i), rightValues.get(i));

						if(!equals){
							break;
						}
					}
				} else

				{
					equals = false;
				}
			} else

			{
				equals = equals(leftValue, rightValue);
			} // End if

			if(!equals){
				return false;
			}
		}

		return true;
	}

	static
	public <E extends PMMLObject> void copyState(E from, E to){
		Class<?> fromClazz = from.getClass();
		Class<?> toClazz = to.getClass();

		// Allow copying to the same class or to a subclass, but not to a superclass
		if(!(fromClazz).isAssignableFrom(toClazz)){
			throw new IllegalArgumentException();
		}

		List<Field> fields = getFields(fromClazz);
		for(Field field : fields){
			Object value = getFieldValue(field, from);

			setFieldValue(field, to, value);
		}
	}

	static
	public Field getField(Class<?> clazz, String name){

		while(clazz != null){

			try {
				Field field = clazz.getDeclaredField(name);

				if(!ReflectionUtil.FIELD_SELECTOR.test(field)){
					throw new IllegalArgumentException(name);
				}

				return field;
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
			result = loadFields(clazz);

			ReflectionUtil.classFields.putIfAbsent(clazz, result);
		}

		return result;
	}

	static
	public List<Field> getAttributeFields(){
		List<Class<?>> clazzes = Arrays.asList(
			org.dmg.pmml.PMMLAttributes.class,
			org.dmg.pmml.association.PMMLAttributes.class,
			org.dmg.pmml.baseline.PMMLAttributes.class,
			org.dmg.pmml.bayesian_network.PMMLAttributes.class,
			org.dmg.pmml.clustering.PMMLAttributes.class,
			org.dmg.pmml.gaussian_process.PMMLAttributes.class,
			org.dmg.pmml.general_regression.PMMLAttributes.class,
			org.dmg.pmml.mining.PMMLAttributes.class,
			org.dmg.pmml.naive_bayes.PMMLAttributes.class,
			org.dmg.pmml.nearest_neighbor.PMMLAttributes.class,
			org.dmg.pmml.neural_network.PMMLAttributes.class,
			org.dmg.pmml.regression.PMMLAttributes.class,
			org.dmg.pmml.rule_set.PMMLAttributes.class,
			org.dmg.pmml.scorecard.PMMLAttributes.class,
			org.dmg.pmml.sequence.PMMLAttributes.class,
			org.dmg.pmml.support_vector_machine.PMMLAttributes.class,
			org.dmg.pmml.text.PMMLAttributes.class,
			org.dmg.pmml.time_series.PMMLAttributes.class,
			org.dmg.pmml.tree.PMMLAttributes.class
		);

		return getClassConstants(clazzes);
	}

	static
	public List<Field> getElementFields(){
		List<Class<?>> clazzes = Arrays.asList(
			org.dmg.pmml.PMMLElements.class,
			org.dmg.pmml.association.PMMLElements.class,
			org.dmg.pmml.baseline.PMMLElements.class,
			org.dmg.pmml.bayesian_network.PMMLElements.class,
			org.dmg.pmml.clustering.PMMLElements.class,
			org.dmg.pmml.gaussian_process.PMMLElements.class,
			org.dmg.pmml.general_regression.PMMLElements.class,
			org.dmg.pmml.mining.PMMLElements.class,
			org.dmg.pmml.naive_bayes.PMMLElements.class,
			org.dmg.pmml.nearest_neighbor.PMMLElements.class,
			org.dmg.pmml.neural_network.PMMLElements.class,
			org.dmg.pmml.regression.PMMLElements.class,
			org.dmg.pmml.rule_set.PMMLElements.class,
			org.dmg.pmml.scorecard.PMMLElements.class,
			org.dmg.pmml.sequence.PMMLElements.class,
			org.dmg.pmml.support_vector_machine.PMMLElements.class,
			org.dmg.pmml.text.PMMLElements.class,
			org.dmg.pmml.time_series.PMMLElements.class,
			org.dmg.pmml.tree.PMMLElements.class
		);

		return getClassConstants(clazzes);
	}

	static
	public Method getGetterMethod(Field field){
		Class<?> clazz = field.getDeclaringClass();

		Map<Field, Method> getterMethods = getGetterMethods(clazz);

		Method getterMethod = getterMethods.get(field);
		if(getterMethod == null){
			throw new RuntimeException(new NoSuchMethodException());
		}

		return getterMethod;
	}

	static
	public Map<Field, Method> getGetterMethods(Class<?> clazz){
		Map<Field, Method> result = ReflectionUtil.classGetterMethods.get(clazz);

		if(result == null){
			result = loadGetterMethods(clazz);

			ReflectionUtil.classGetterMethods.putIfAbsent(clazz, result);
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

	@SuppressWarnings (
		value = {"unchecked"}
	)
	static
	public <E> E getGetterMethodValue(Method method, Object object){

		try {
			return (E)method.invoke(object);
		} catch(IllegalAccessException | InvocationTargetException e){
			throw new RuntimeException(e);
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
	public List<Field> getClassConstants(List<Class<?>> clazzes){
		List<Field> result = new ArrayList<>();

		Function<Field, Field> function = new Function<Field, Field>(){

			@Override
			public Field apply(Field field){

				try {
					return (Field)field.get(null);
				} catch(ReflectiveOperationException roe){
					throw new RuntimeException(roe);
				}
			}
		};

		for(Class<?> clazz : clazzes){
			Field[] fields = clazz.getDeclaredFields();

			for(Field field : fields){
				result.add(function.apply(field));
			}
		}

		return result;
	}

	static
	private Object standardizeValue(Object value){

		if(value instanceof List){
			List<?> list = (List<?>)value;

			if(list.isEmpty()){
				return null;
			}
		}

		return value;
	}

	static
	private List<Field> loadFields(Class<?> clazz){
		List<Field> result = new ArrayList<>();

		while(clazz != null){
			Field[] fields = clazz.getDeclaredFields();

			for(Field field : fields){

				if(!ReflectionUtil.FIELD_SELECTOR.test(field)){
					continue;
				}

				result.add(field);
			}

			clazz = clazz.getSuperclass();
		}

		return Collections.unmodifiableList(result);
	}

	static
	private Map<Field, Method> loadGetterMethods(Class<?> clazz){
		Map<Field, Method> result = new LinkedHashMap<>();

		Map<String, Field> fieldMap = new HashMap<>();

		List<Field> fields = getFields(clazz);
		for(Field field : fields){
			String name = field.getName();

			fieldMap.put(name.toLowerCase(), field);
		}

		Method[] methods = clazz.getMethods();
		for(Method method : methods){
			String name = method.getName();
			Class<?>[] parameterTypes = method.getParameterTypes();

			if(name.startsWith("is")){
				name = name.substring("is".length());
			} else

			if(name.startsWith("get")){
				name = name.substring("get".length());
			} else

			{
				continue;
			} // End if

			if(parameterTypes.length != 0){
				continue;
			}

			Field field = fieldMap.get(name.toLowerCase());
			if(field == null){
				continue;
			}

			result.put(field, method);
		}

		return result;
	}

	static
	private final Predicate<Field> FIELD_SELECTOR = new Predicate<Field>(){

		@Override
		public boolean test(Field field){

			if(hasValidName(field)){
				int modifiers = field.getModifiers();

				return !Modifier.isStatic(modifiers);
			}

			return false;
		}

		private boolean hasValidName(Field field){
			String name = field.getName();

			if(name.length() > 0){
				return Character.isLetterOrDigit(name.charAt(0));
			}

			return false;
		}
	};

	private static final ConcurrentMap<Class<?>, List<Field>> classFields = new ConcurrentHashMap<>();

	private static final ConcurrentMap<Class<?>, Map<Field, Method>> classGetterMethods = new ConcurrentHashMap<>();

	private static final Set<Class<?>> primitiveWrapperClasses = new HashSet<>(Arrays.<Class<?>>asList(Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Character.class));
}