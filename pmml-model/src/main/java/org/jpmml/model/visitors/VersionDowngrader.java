/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Objects;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.annotations.Added;
import org.jpmml.model.annotations.Optional;
import org.jpmml.model.annotations.Removed;
import org.jpmml.model.annotations.Required;
import org.jpmml.model.filters.ExportFilter;

/**
 * <p>
 * A Visitor that downgrades a class model object from the latest PMML schema version to some earlier one.
 * </p>
 *
 * @see ExportFilter
 */
public class VersionDowngrader extends VersionInspector {

	private Version version = null;


	public VersionDowngrader(Version version){
		this.version = Objects.requireNonNull(version);

		if(!version.isStandard()){
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void handleAdded(PMMLObject object, AnnotatedElement element, Added added){
		Version version = added.value();

		if(version.isStandard() && version.compareTo(this.version) > 0){

			if(element instanceof Class){
				// Ignored
			} else

			if(element instanceof Field){
				Field field = (Field)element;

				if(added.removable()){
					ReflectionUtil.setFieldValue(field, object, null);
				}
			} else

			{
				throw new IllegalArgumentException();
			}
		}
	}

	@Override
	public void handleRemoved(PMMLObject object, AnnotatedElement element, Removed removed){
	}

	@Override
	public void handleOptional(PMMLObject object, AnnotatedElement element, Optional optional){
	}

	@Override
	public void handleRequired(PMMLObject object, AnnotatedElement element, Required required){
	}
}