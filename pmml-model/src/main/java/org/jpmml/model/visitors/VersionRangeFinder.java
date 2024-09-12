/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;

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

	@Override
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

	@Override
	public void updateMaximum(PMMLObject object, AnnotatedElement element, Version maximum){

		if(maximum != null && maximum.compareTo(this.maximum) < 0){
			this.maximum = maximum;
		}
	}
}