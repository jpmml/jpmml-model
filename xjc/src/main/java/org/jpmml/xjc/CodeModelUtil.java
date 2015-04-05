/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.xjc;

import java.util.List;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JType;

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
}