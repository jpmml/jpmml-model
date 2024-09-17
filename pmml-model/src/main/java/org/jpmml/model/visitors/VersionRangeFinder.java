/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.jpmml.model.annotations.Added;
import org.jpmml.model.annotations.Optional;
import org.jpmml.model.annotations.Removed;
import org.jpmml.model.annotations.Required;

/**
 * <p>
 * A Visitor that determines the range of valid PMML schema versions for a class model object.
 * </p>
 */
public class VersionRangeFinder extends VersionInspector implements Resettable {

	private Version minimum = Version.getMinimum();

	private Version maximum = Version.getMaximum();


	@Override
	public void reset(){
		this.minimum = Version.getMinimum();
		this.maximum = Version.getMaximum();
	}

	@Override
	public void handleAdded(PMMLObject object, AnnotatedElement element, Added added){
		updateMinimum(object, element, added.value());
	}

	@Override
	public void handleRemoved(PMMLObject object, AnnotatedElement element, Removed removed){
		updateMaximum(object, element, removed.value());
	}

	@Override
	public void handleOptional(PMMLObject object, AnnotatedElement element, Optional optional){
		updateMinimum(object, element, optional.value());
	}

	@Override
	public void handleRequired(PMMLObject object, AnnotatedElement element, Required required){
		updateMaximum(object, element, (required.value()).previous());
	}

	/**
	 * <p>
	 * The minimum (ie. earliest) PMML schema version that can fully represent this class model object.
	 * </p>
	 *
	 * @see Version#getMinimum()
	 */
	public Version getMinimum(){
		return this.minimum;
	}

	public void updateMinimum(PMMLObject object, AnnotatedElement element, Version minimum){

		if(minimum != null && minimum.compareTo(this.minimum) > 0){
			this.minimum = minimum;
		}
	}

	/**
	 * <p>
	 * The maximum (ie. latest) PMML schema version that can fully represent this class model object.
	 * </p>
	 *
	 * @see Version#getMaximum()
	 */
	public Version getMaximum(){
		return this.maximum;
	}

	public void updateMaximum(PMMLObject object, AnnotatedElement element, Version maximum){

		if(maximum != null && maximum.compareTo(this.maximum) < 0){
			this.maximum = maximum;
		}
	}
}