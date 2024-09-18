/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.jpmml.model.MarkupException;
import org.jpmml.model.MisplacedAttributeException;
import org.jpmml.model.MisplacedElementException;
import org.jpmml.model.MissingAttributeException;
import org.jpmml.model.MissingElementException;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.UnsupportedAttributeException;
import org.jpmml.model.UnsupportedElementException;
import org.jpmml.model.annotations.Added;
import org.jpmml.model.annotations.Optional;
import org.jpmml.model.annotations.Removed;
import org.jpmml.model.annotations.Required;

public class VersionChecker extends VersionInspector implements Resettable {

	private Version version = null;

	private List<MarkupException> exceptions = new ArrayList<>();


	public VersionChecker(Version version){
		this.version = Objects.requireNonNull(version);
	}

	@Override
	public void reset(){
		this.exceptions.clear();
	}

	@Override
	public void handleAdded(PMMLObject object, AnnotatedElement element, Added added){
		Version version = added.value();

		if(version != null && version.compareTo(this.version) > 0){

			if(element instanceof Field){
				Field field = (Field)element;

				Object value = ReflectionUtil.getFieldValue(field, object);

				if(isAttribute(field)){
					report(new UnsupportedAttributeException(object, field, value));
				} else

				if(isEnumValue(field)){
					report(new UnsupportedAttributeException(object, (Enum<?>)value));
				} else

				if(isElement(field)){
					report(new UnsupportedElementException((PMMLObject)value));
				} else

				{
					throw new IllegalArgumentException();
				}
			} else

			{
				report(new UnsupportedElementException(object));
			}
		}
	}

	@Override
	public void handleRemoved(PMMLObject object, AnnotatedElement element, Removed removed){
		Version version = (removed.value()).previous();

		if(version != null && version.compareTo(this.version) < 0){

			if(element instanceof Field){
				Field field = (Field)element;

				Object value = ReflectionUtil.getFieldValue(field, object);

				if(isAttribute(field)){
					report(new MisplacedAttributeException(object, field, value));
				} else

				if(isElement(field)){
					report(new MisplacedElementException((PMMLObject)value));
				} else

				{
					throw new IllegalArgumentException();
				}
			} else

			{
				report(new MisplacedElementException(object));
			}
		}
	}

	@Override
	public void handleOptional(PMMLObject object, AnnotatedElement element, Optional optional){
		Version version = optional.value();

		if(version != null && version.compareTo(this.version) > 0){

			if(element instanceof Field){
				Field field = (Field)element;

				if(isAttribute(field)){
					report(new MissingAttributeException(object, field));
				} else

				if(isElement(field)){
					report(new MissingElementException(object, field));
				} else

				{
					throw new IllegalArgumentException();
				}
			} else

			{
				throw new IllegalArgumentException();
			}
		}
	}

	@Override
	public void handleRequired(PMMLObject object, AnnotatedElement element, Required required){
		Version version = required.value();

		if(version != null && version.compareTo(this.version) <= 0){

			if(element instanceof Field){
				Field field = (Field)element;

				if(isAttribute(field)){
					report(new MissingAttributeException(object, field));
				} else

				if(isElement(field)){
					report(new MissingElementException(object, field));
				} else

				{
					throw new IllegalArgumentException();
				}
			} else

			{
				throw new IllegalArgumentException();
			}
		}
	}

	protected void report(MarkupException exception){
		this.exceptions.add(exception);
	}

	public List<MarkupException> getExceptions(){
		return this.exceptions;
	}
}