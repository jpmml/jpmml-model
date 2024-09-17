/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.dmg.pmml.VersionUtil;
import org.jpmml.model.annotations.Since;

public class ExtendedVersionRangeFinder extends VersionRangeFinder {

	private String libraryMinimum = null;

	private String libraryMaximum = null;


	@Override
	public void reset(){
		super.reset();

		this.libraryMinimum = null;
		this.libraryMaximum = null;
	}

	@Override
	public void updateMinimum(PMMLObject object, AnnotatedElement element, Version minimum){
		Since since = element.getAnnotation(Since.class);

		if(since != null){
			String libraryMinimum = since.value();

			if(this.libraryMinimum == null || VersionUtil.compare(libraryMinimum, this.libraryMinimum) < 0){
				this.libraryMinimum = libraryMinimum;
			}
		} else

		{
			super.updateMinimum(object, element, minimum);
		}
	}

	@Override
	public void updateMaximum(PMMLObject object, AnnotatedElement element, Version maximum){
		Since since = element.getAnnotation(Since.class);

		if(since != null){
			String libraryMaximum = since.value();

			if(this.libraryMaximum == null || VersionUtil.compare(libraryMaximum, this.libraryMaximum) > 0){
				this.libraryMaximum = libraryMaximum;
			}
		} else

		{
			super.updateMaximum(object, element, maximum);
		}
	}

	public String getLibraryMinimum(){
		return this.libraryMinimum;
	}

	public String getLibraryMaximum(){
		return this.libraryMaximum;
	}
}