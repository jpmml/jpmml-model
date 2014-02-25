/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.xjc;

import java.util.*;

import com.sun.codemodel.*;
import com.sun.tools.xjc.model.*;
import com.sun.tools.xjc.outline.*;

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