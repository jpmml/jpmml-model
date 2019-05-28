/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

/**
 * <p>
 * A Visitor that sets redundant attribute values to <code>null</code>.
 * </p>
 */
public class AttributeCleaner extends AbstractVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		Map<Field, Method> getterMethods = ReflectionUtil.getGetterMethods(object.getClass());

		Collection<Map.Entry<Field, Method>> entries = getterMethods.entrySet();
		for(Map.Entry<Field, Method> entry : entries){
			Field field = entry.getKey();
			Method getterMethod = entry.getValue();

			XmlAttribute attribute = field.getAnnotation(XmlAttribute.class);
			if(attribute == null || attribute.required()){
				continue;
			}

			Object fieldValue = ReflectionUtil.getFieldValue(field, object);
			if(fieldValue != null){
				Object getterMethodValue = ReflectionUtil.getGetterMethodValue(getterMethod, object);

				if(Objects.equals(fieldValue, getterMethodValue)){
					ReflectionUtil.setFieldValue(field, object, null);

					getterMethodValue = ReflectionUtil.getGetterMethodValue(getterMethod, object);
					if(getterMethodValue != null && !Objects.equals(fieldValue, getterMethodValue)){
						ReflectionUtil.setFieldValue(field, object, fieldValue);
					}
				}
			}
		}

		return super.visit(object);
	}
}