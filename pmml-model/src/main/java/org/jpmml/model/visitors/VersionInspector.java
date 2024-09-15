/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.dmg.pmml.Apply;
import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.dmg.pmml.VersionUtil;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.annotations.Added;
import org.jpmml.model.annotations.Optional;
import org.jpmml.model.annotations.Removed;
import org.jpmml.model.annotations.Required;

/**
 * @see Added
 * @see Optional
 * @see Removed
 * @see Required
 */
abstract
public class VersionInspector extends AbstractVisitor {

	abstract
	public void updateMinimum(PMMLObject object, AnnotatedElement element, Version minimum);

	abstract
	public void updateMaximum(PMMLObject object, AnnotatedElement element, Version maximum);

	@Override
	public VisitorAction visit(PMMLObject object){

		for(Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()){
			inspect(object, clazz);
		}

		List<Field> fields = ReflectionUtil.getFields(object.getClass());
		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			inspect(object, field, value);

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

				inspect(object, enumField);
			}
		}

		return super.visit(object);
	}

	@Override
	public VisitorAction visit(Apply apply){
		String function = apply.requireFunction();

		Version version = VersionUtil.getVersion(function);
		if(version != null){
			updateMinimum(apply, PMMLAttributes.APPLY_FUNCTION, version);
		}

		return super.visit(apply);
	}

	private void inspect(PMMLObject object, Field field, Object value){
		Class<?> type = field.getType();

		if(type.isPrimitive()){

			if(ReflectionUtil.isDefaultValue(value)){
				return;
			}
		} else

		{
			if(isNull(value)){
				Optional optional = field.getAnnotation(Optional.class);
				if(optional != null){
					updateMinimum(object, field, optional.value());
				}

				Required required = field.getAnnotation(Required.class);
				if(required != null){
					updateMaximum(object, field, (required.value()).previous());
				}

				return;
			}
		}

		inspect(object, field);
	}

	private void inspect(PMMLObject object, AnnotatedElement element){
		Added added = element.getAnnotation(Added.class);
		if(added != null){
			updateMinimum(object, element, added.value());
		}

		Removed removed = element.getAnnotation(Removed.class);
		if(removed != null){
			updateMaximum(object, element, removed.value());
		}
	}

	static
	private boolean isNull(Object value){

		if(value instanceof Collection){
			Collection<?> collection = (Collection<?>)value;

			return collection.isEmpty();
		}

		return (value == null);
	}
}