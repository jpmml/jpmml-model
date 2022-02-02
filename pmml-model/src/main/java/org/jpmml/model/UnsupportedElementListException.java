/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.List;

import org.dmg.pmml.PMMLObject;

public class UnsupportedElementListException extends UnsupportedMarkupException {

	public UnsupportedElementListException(List<? extends PMMLObject> objects){
		super("List of elements " + XPathUtil.formatElement((objects.get(0)).getClass()) + " is not supported", objects.get(0));
	}
}