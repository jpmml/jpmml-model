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
	public FieldOutline findField(FieldOutline[] fields, FieldFilter filter){
		FieldOutline[] acceptedFields = filterFields(fields, filter);

		if(acceptedFields.length == 0){
			return null;
		} else

		if(acceptedFields.length == 1){
			return acceptedFields[0];
		} else

		{
			throw new IllegalArgumentException();
		}
	}

	static
	public FieldOutline[] filterFields(FieldOutline[] fields, FieldFilter filter){
		List<FieldOutline> result = new ArrayList<>();

		for(FieldOutline field : fields){

			if(filter.accept(field)){
				result.add(field);
			}
		}

		return result.toArray(new FieldOutline[result.size()]);
	}
}