/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlEnumValue;
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
	public void handleAdded(PMMLObject object, AnnotatedElement element, Added added);

	abstract
	public void handleRemoved(PMMLObject object, AnnotatedElement element, Removed removed);

	abstract
	public void handleOptional(PMMLObject object, AnnotatedElement element, Optional optional);

	abstract
	public void handleRequired(PMMLObject object, AnnotatedElement element, Required required);

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
		String function = apply.getFunction();

		Version version = VersionUtil.getVersion(function);
		if(version != null){
			Added added = new Added(){

				@Override
				public Class<? extends Annotation> annotationType(){
					return Added.class;
				}

				@Override
				public Version value(){
					return version;
				}

				@Override
				public boolean removable(){
					return false;
				}
			};

			handleAdded(apply, PMMLAttributes.APPLY_FUNCTION, added);
		}

		return super.visit(apply);
	}

	private void inspect(PMMLObject object, AnnotatedElement element){
		Added added = element.getAnnotation(Added.class);
		if(added != null){
			handleAdded(object, element, added);
		}

		Removed removed = element.getAnnotation(Removed.class);
		if(removed != null){
			handleRemoved(object, element, removed);
		}
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
					handleOptional(object, field, optional);
				}

				Required required = field.getAnnotation(Required.class);
				if(required != null){
					handleRequired(object, field, required);
				}

				return;
			}
		}

		inspect(object, field);
	}

	static
	private boolean isNull(Object value){

		if(value instanceof Collection){
			Collection<?> collection = (Collection<?>)value;

			return collection.isEmpty();
		}

		return (value == null);
	}

	static
	protected boolean isAttribute(Field field){
		XmlAttribute xmlAttribute = field.getAnnotation(XmlAttribute.class);

		return (xmlAttribute != null);
	}

	static
	protected boolean isEnumValue(Field field){
		XmlEnumValue xmlEnumValue = field.getAnnotation(XmlEnumValue.class);

		return (xmlEnumValue != null);
	}

	static
	protected boolean isElement(Field field){
		XmlElement xmlElement = field.getAnnotation(XmlElement.class);
		XmlElements xmlElements = field.getAnnotation(XmlElements.class);

		return (xmlElement != null) || (xmlElements != null);
	}
}