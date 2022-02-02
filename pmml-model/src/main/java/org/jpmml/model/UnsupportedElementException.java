/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.PMMLObject;

public class UnsupportedElementException extends UnsupportedMarkupException {

	public UnsupportedElementException(PMMLObject object){
		super("Element " + formatElement(object) + " is not supported", object);
	}

	static
	private String formatElement(PMMLObject object){
		Class<? extends PMMLObject> clazz = object.getClass();

		String result = XPathUtil.formatElement(clazz);

		String name = clazz.getName();
		if(!name.startsWith("org.dmg.pmml.")){
			result += (" (Java class " + name + ")");
		}

		return result;
	}
}