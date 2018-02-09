/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.xjc;

import java.util.Arrays;
import java.util.function.Predicate;

import com.sun.tools.xjc.outline.FieldOutline;

public class XJCUtil {

	private XJCUtil(){
	}

	static
	public FieldOutline findSingletonField(FieldOutline[] fieldOutlines, Predicate<FieldOutline> predicate){
		FieldOutline[] acceptedFieldOutlines = filterFields(fieldOutlines, predicate);

		if(acceptedFieldOutlines.length == 0){
			return null;
		} else

		if(acceptedFieldOutlines.length == 1){
			return acceptedFieldOutlines[0];
		} else

		{
			throw new IllegalArgumentException();
		}
	}

	static
	public FieldOutline[] filterFields(FieldOutline[] fieldOutlines, Predicate<FieldOutline> predicate){
		return Arrays.stream(fieldOutlines).filter(predicate).toArray(FieldOutline[]::new);
	}
}