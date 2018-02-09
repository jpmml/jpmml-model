/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.xjc;

import java.util.ArrayList;
import java.util.List;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.outline.FieldOutline;

public class CodeModelUtil {

	private CodeModelUtil(){
	}

	static
	public JType getElementType(JType collectionType){
		JClass collectionClazz = (JClass)collectionType;

		List<JClass> elementTypes = collectionClazz.getTypeParameters();
		if(elementTypes.size() != 1){
			throw new IllegalArgumentException();
		}

		return elementTypes.get(0);
	}

	static
	public FieldOutline findField(FieldOutline[] fieldOutlines, FieldFilter filter){
		FieldOutline[] acceptedFieldOutlines = filterFields(fieldOutlines, filter);

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
	public FieldOutline[] filterFields(FieldOutline[] fieldOutlines, FieldFilter filter){
		List<FieldOutline> result = new ArrayList<>();

		for(FieldOutline fieldOutline : fieldOutlines){

			if(filter.accept(fieldOutline)){
				result.add(fieldOutline);
			}
		}

		return result.toArray(new FieldOutline[result.size()]);
	}
}