/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.List;

import org.dmg.pmml.PMMLObject;

public class InvalidElementListException extends InvalidMarkupException {

	public InvalidElementListException(List<? extends PMMLObject> objects){
		super("List of elements " + XPathUtil.formatElement((objects.get(0)).getClass()) + " is not valid", objects.get(0));
	}
}