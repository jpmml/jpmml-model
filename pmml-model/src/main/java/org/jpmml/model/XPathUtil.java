/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.dmg.pmml.PMMLObject;

public class XPathUtil {

	private XPathUtil(){
	}

	static
	public String formatElement(Class<? extends PMMLObject> elementClazz){
		return getElementName(elementClazz);
	}

	static
	public String formatElementOrAttribute(Field field){
		return formatElementOrAttribute((Class)field.getDeclaringClass(), field);
	}

	static
	public String formatElementOrAttribute(Class<? extends PMMLObject> elementClazz, Field field){
		XmlElement element = field.getAnnotation(XmlElement.class);
		XmlElements elements = field.getAnnotation(XmlElements.class);
		XmlAttribute attribute = field.getAnnotation(XmlAttribute.class);

		if(element != null){
			Class<?> childElementClazz = getElementType(field);

			try {
				return getElementName(elementClazz) + "/" + getElementName(childElementClazz);
			} catch(IllegalArgumentException iae){
				return getElementName(elementClazz) + "/" + element.name();
			}
		} else

		if(elements != null){
			Class<?> childElementClazz = getElementType(field);

			return getElementName(elementClazz) + "/<" + childElementClazz.getSimpleName() + ">";
		} else

		if(attribute != null){
			return getElementName(elementClazz) + "@" + attribute.name();
		}

		throw new IllegalArgumentException();
	}

	static
	public String formatAttribute(Field field, Object value){
		return formatAttribute((Class)field.getDeclaringClass(), field, value);
	}

	static
	public String formatAttribute(Class<? extends PMMLObject> elementClazz, Field field, Object value){
		XmlAttribute attribute = field.getAnnotation(XmlAttribute.class);

		if(attribute != null){
			return formatElementOrAttribute(elementClazz, field) + (value != null ? ("=" + String.valueOf(value)) : "");
		}

		throw new IllegalArgumentException();
	}

	static
	public Class<?> getElementType(Field field){
		Class<?> childElementClazz = field.getType();

		if((List.class).isAssignableFrom(childElementClazz)){
			ParameterizedType listType = (ParameterizedType)field.getGenericType();

			Type[] typeArguments = listType.getActualTypeArguments();
			if(typeArguments.length != 1){
				throw new IllegalArgumentException();
			}

			childElementClazz = (Class<?>)typeArguments[0];
		}

		return childElementClazz;
	}

	static
	private String getElementName(Class<?> clazz){

		while(clazz != null){
			XmlRootElement rootElement = clazz.getAnnotation(XmlRootElement.class);

			if(rootElement != null){
				return rootElement.name();
			}

			clazz = clazz.getSuperclass();
		}

		throw new IllegalArgumentException();
	}
}