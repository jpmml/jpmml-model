/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import org.dmg.pmml.PMMLObject;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.annotations.CollectionElementType;

abstract
public class Interner<V> extends AbstractVisitor {

	private Class<? extends V> type = null;


	public Interner(Class<? extends V> type){
		setType(type);
	}

	abstract
	public V intern(V value);

	public Class<? extends V> getType(){
		return this.type;
	}

	private void setType(Class<? extends V> type){
		this.type = Objects.requireNonNull(type);
	}

	protected void apply(Field field, PMMLObject object){
		Class<? extends V> type = getType();

		Class<?> fieldType = field.getType();

		if(Objects.equals(List.class, fieldType)){
			CollectionElementType collectionElementType = field.getAnnotation(CollectionElementType.class);

			if(collectionElementType == null){
				throw new IllegalArgumentException();
			}

			Class<?> elementClazz = collectionElementType.value();
			if(elementClazz.isAssignableFrom(type)){
				List<V> values = (List<V>)ReflectionUtil.getFieldValue(field, object);

				if(values != null && !values.isEmpty()){

					for(ListIterator<V> it = values.listIterator(); it.hasNext(); ){
						V value = it.next();

						if(type.isInstance(value)){
							V internedValue = intern(value);

							it.set(internedValue);
						}
					}
				}
			}

			return;
		} // End if

		if(fieldType.isAssignableFrom(type)){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(value != null){

				if(type.isInstance(value)){
					V internedValue = intern(type.cast(value));

					ReflectionUtil.setFieldValue(field, object, internedValue);
				}
			}
		}
	}
}