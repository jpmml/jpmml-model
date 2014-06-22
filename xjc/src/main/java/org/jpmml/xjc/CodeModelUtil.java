/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.xjc;

import java.util.List;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;

public class CodeModelUtil {

	private CodeModelUtil(){
	}

	static
	public JFieldVar getFieldVar(FieldOutline field){
		ClassOutline clazz = field.parent();

		CPropertyInfo propertyInfo = field.getPropertyInfo();

		return (clazz.implClass.fields()).get(propertyInfo.getName(false));
	}

	static
	public JType getElementType(JType collectionType){
		JClass collectionClazz = (JClass)collectionType;

		List<JClass> elementTypes = collectionClazz.getTypeParameters();

		return elementTypes.get(0);
	}
}